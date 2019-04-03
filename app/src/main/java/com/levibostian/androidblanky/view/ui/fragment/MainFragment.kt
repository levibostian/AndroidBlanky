package com.levibostian.androidblanky.view.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.view.ui.activity.LaunchActivity
import com.levibostian.androidblanky.view.ui.activity.LicensesActivity
import com.levibostian.androidblanky.view.ui.activity.SettingsActivity
import com.levibostian.androidblanky.view.ui.adapter.ReposRecyclerViewAdapter
import com.levibostian.androidblanky.view.ui.dialog.AreYouSureLogoutWendyDialogFragment
import com.levibostian.androidblanky.view.ui.extensions.closeKeyboard
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainFragment: Fragment() {

    private var fetchingSnackbar: Snackbar? = null

    private val reposViewModel: ReposViewModel by viewModel()
    private val gitHubUsernameViewModel: GitHubUsernameViewModel by viewModel()

    companion object {
        fun newInstance(): MainFragment = MainFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                startActivity(SettingsActivity.getIntent(activity!!))
                true
            }
            R.id.open_source_licenses -> {
                startActivity(LicensesActivity.getIntent(activity!!))
                true
            }
            R.id.logout -> {
                startActivity(LaunchActivity.getIntent(activity!!, true))
                activity!!.finish()
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
                    reposState.whenNoCache { _, errorDuringFetch ->
                        frag_main_loading_empty.showLoadingView(false)

                        errorDuringFetch?.message?.let {
                            frag_main_loading_empty.showEmptyView(false)
                            frag_main_loading_empty.emptyViewMessage = it
                        }
                    }

                    reposState.whenCache { cache, lastSuccessfulFetch, isFetching, _, _ ->
                        data_age_textview.init {
                            getString(R.string.repos_last_updated).format(Locale.getDefault(), DateUtils.getRelativeTimeSpanString(lastSuccessfulFetch.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS))
                        }

                        if (cache == null) {
                            frag_main_loading_empty.emptyViewMessage = "There are no repos."
                            frag_main_loading_empty.showEmptyView(false)
                        } else {
                            repos_recyclerview.apply {
                                layoutManager = LinearLayoutManager(activity!!)
                                adapter = ReposRecyclerViewAdapter(cache)
                                setHasFixedSize(true)
                            }

                            frag_main_loading_empty.showContentView(true)
                        }

                        if (isFetching) {
                            fetchingSnackbar = Snackbar.make(parent_view, "Updating repos list...", Snackbar.LENGTH_LONG)
                            fetchingSnackbar?.show()
                        } else {
                            fetchingSnackbar?.dismiss()
                        }
                    }
                })
        gitHubUsernameViewModel.observeUsername()
                .observe(this, Observer { username ->
                    if (username.cache != null) {
                        username_edittext.setText(username.cache, TextView.BufferType.EDITABLE)
                        reposViewModel.setUsername(username.cache!!)
                    } else {
                        username_edittext.setText("", TextView.BufferType.EDITABLE)
                    }
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

}
