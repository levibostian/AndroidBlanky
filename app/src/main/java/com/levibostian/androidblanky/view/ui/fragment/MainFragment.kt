package com.levibostian.androidblanky.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextWatcher
import android.text.format.DateUtils
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.view.ui.adapter.ReposRecyclerViewAdapter
import com.levibostian.androidblanky.view.ui.extensions.closeKeyboard
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.levibostian.androidblanky.extensions.onAttachDiGraph
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import javax.inject.Inject

class MainFragment: Fragment() {

    private var fetchingSnackbar: Snackbar? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var eventBus: EventBus
    private val reposViewModel by viewModels<ReposViewModel> { viewModelFactory }
    private val gitHubUsernameViewModel by viewModels<GitHubUsernameViewModel> { viewModelFactory }

    enum class SwapperViews {
        LOADING_VIEW,
        EMPTY_VIEW,
        REPOS
    }

    override fun onAttach(context: Context) {
        onAttachDiGraph().inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController()) || when (item.itemId) {
            R.id.logout -> {
                eventBus.post(LogoutUserEvent())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        frag_main_swapper.viewMap = mapOf(
                Pair(SwapperViews.EMPTY_VIEW.name, frag_main_empty),
                Pair(SwapperViews.LOADING_VIEW.name, frag_main_loading),
                Pair(SwapperViews.REPOS.name, frag_main_content)
        )
    }

    override fun onStart() {
        super.onStart()

        reposViewModel.observeRepos()
                .observe(this, Observer { reposState ->
                    reposState.whenNoCache { _, errorDuringFetch ->
                        frag_main_swapper.swapTo(SwapperViews.LOADING_VIEW.name) {}

                        errorDuringFetch?.message?.let {
                            frag_main_empty.message = it
                            frag_main_swapper.swapTo(SwapperViews.EMPTY_VIEW.name) {}
                        }
                    }

                    reposState.whenCache { cache, lastSuccessfulFetch, isFetching, _, _ ->
                        data_age_textview.init {
                            getString(R.string.repos_last_updated).format(Locale.getDefault(), DateUtils.getRelativeTimeSpanString(lastSuccessfulFetch.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS))
                        }

                        if (cache == null) {
                            frag_main_empty.message = getString(R.string.user_no_repos)
                            frag_main_swapper.swapTo(SwapperViews.EMPTY_VIEW.name) {}
                        } else {
                            repos_recyclerview.apply {
                                layoutManager = LinearLayoutManager(requireActivity())
                                adapter = ReposRecyclerViewAdapter(cache)
                                setHasFixedSize(true)
                            }

                            frag_main_swapper.swapTo(SwapperViews.REPOS.name) {}
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

        username_edittext.errorListener = { text ->
            gitHubUsernameViewModel.validateUsername(text)
        }

        go_button.setOnClickListener {
            username_edittext.textIfValid?.let { username ->
                gitHubUsernameViewModel.setUsername(username.toString())
                closeKeyboard()
            }
        }
    }

}
