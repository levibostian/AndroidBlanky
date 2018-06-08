package com.levibostian.androidblanky.view.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.R
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.recyclerview.R.attr.layoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.view.ui.activity.LicensesActivity
import com.levibostian.androidblanky.view.ui.activity.MainActivity
import com.levibostian.androidblanky.view.ui.activity.SettingsActivity
import com.levibostian.androidblanky.view.ui.adapter.ReposRecyclerViewAdapter
import com.levibostian.androidblanky.view.ui.extensions.closeKeyboard
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import com.levibostian.teller.datastate.listener.LocalDataStateListener
import com.levibostian.teller.datastate.listener.OnlineDataStateListener
import org.greenrobot.eventbus.EventBus
import java.util.*

class MainFragment : Fragment() {

    private lateinit var reposViewModel: ReposViewModel
    private lateinit var gitHubUsernameViewModel: GitHubUsernameViewModel

    private var updateLastSyncedHandler: Handler? = null
    private var updateLastSyncedRunnable: Runnable? = null

    private var fetchingSnackbar: Snackbar? = null

    @Inject lateinit var service: GitHubService
    @Inject lateinit var db: Database
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var eventBus: EventBus

    companion object {
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val bundle = Bundle()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as MainApplication).component.inject(this)
        reposViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReposViewModel::class.java)
        gitHubUsernameViewModel = ViewModelProviders.of(this, viewModelFactory).get(GitHubUsernameViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.settings -> {
                startActivity(SettingsActivity.getIntent(activity!!))
                true
            }
            R.id.open_source_licenses -> {
                startActivity(LicensesActivity.getIntent(activity!!))
                true
            }
            R.id.logout -> {
                eventBus.post(LogoutUserEvent())
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        reposViewModel.observeRepos()
                .observe(this, Observer { reposState ->
                    reposState?.deliver(object : OnlineDataStateListener<List<RepoModel>> {
                        override fun finishedFirstFetchOfData(errorDuringFetch: Throwable?) {
                            if (errorDuringFetch != null) {
                                errorDuringFetch.message?.let {
                                    frag_main_loading_empty.showEmptyView(false)
                                    frag_main_loading_empty.emptyViewMessage = it
                                }

                                activity?.let {
                                    AlertDialog.Builder(it)
                                            .setTitle("Error")
                                            .setMessage(errorDuringFetch.message?: "Unknown error. Please, try again.")
                                            .setPositiveButton("Ok") { dialog, _ ->
                                                dialog.dismiss()
                                            }
                                            .create()
                                            .show()
                                }
                            }
                        }
                        override fun firstFetchOfData() {
                            frag_main_loading_empty.showLoadingView(false)
                        }
                        override fun cacheEmpty() {
                            frag_main_loading_empty.showEmptyView(false)
                            frag_main_loading_empty.emptyViewMessage = "There are no repos."
                        }
                        override fun cacheData(data: List<RepoModel>, fetched: Date) {
                            frag_main_loading_empty.showContentView(true)

                            updateLastSyncedRunnable?.let { updateLastSyncedHandler?.removeCallbacks(it) }
                            updateLastSyncedHandler = Handler()
                            updateLastSyncedRunnable = Runnable {
                                data_age_textview.text = "Data last synced ${DateUtils.getRelativeTimeSpanString(fetched.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)}"
                                updateLastSyncedHandler?.postDelayed(updateLastSyncedRunnable!!, 1000)
                            }
                            updateLastSyncedHandler?.post(updateLastSyncedRunnable!!)

                            repos_recyclerview.apply {
                                layoutManager = LinearLayoutManager(activity!!)
                                adapter = ReposRecyclerViewAdapter(data)
                                setHasFixedSize(true)
                            }
                        }
                        override fun fetchingFreshCacheData() {
                            fetchingSnackbar = Snackbar.make(parent_view, "Updating repos list...", Snackbar.LENGTH_LONG)
                            fetchingSnackbar?.show()
                        }
                        override fun finishedFetchingFreshCacheData(errorDuringFetch: Throwable?) {
                            fetchingSnackbar?.dismiss()
                        }
                    })
                })
        gitHubUsernameViewModel.observeUsername()
                .observe(this, Observer { username ->
                    username?.deliver(object : LocalDataStateListener<String> {
                        override fun isEmpty() {
                            username_edittext.setText("", TextView.BufferType.EDITABLE)
                        }
                        override fun data(data: String) {
                            username_edittext.setText(data, TextView.BufferType.EDITABLE)
                            reposViewModel.setUsername(data)
                        }
                    })
                })

        go_button.setOnClickListener {
            if (username_edittext.text.isBlank()) {
                username_edittext.error = "Enter a GitHub username"
            } else {
                gitHubUsernameViewModel.setUsername(username_edittext.text.toString())

                closeKeyboard()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        updateLastSyncedRunnable?.let { updateLastSyncedHandler?.removeCallbacks(it) }
    }

}
