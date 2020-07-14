package com.levibostian.view.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.levibostian.R
import com.levibostian.extensions.onAttachDiGraph
import com.levibostian.service.ResetAppRunner
import com.levibostian.service.service.ViewDataProvider
import com.levibostian.service.type.list_item.RepoListItem
import com.levibostian.view.ui.adapter.ReposRecyclerViewAdapter
import com.levibostian.view.ui.extensions.closeKeyboard
import com.levibostian.viewmodel.ReposViewModel
import com.squareup.moshi.JsonClass
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import javax.inject.Inject

class MainFragment : Fragment() {

    private var fetchingSnackbar: Snackbar? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var resetAppRunner: ResetAppRunner
    @Inject lateinit var viewDataProvider: ViewDataProvider
    private val reposViewModel by viewModels<ReposViewModel> { viewModelFactory }

    private val recyclerViewAdapter: ReposRecyclerViewAdapter
        get() = repos_recyclerview.adapter as ReposRecyclerViewAdapter

    private val viewData: ViewData by lazy {
        viewDataProvider.mainFragment
    }

    // The pattern of ViewData is to have it exist in the View controller itself and not share the Vo with other parts of the app. Why? Because even if you imagine 1 view being shared across multiple screens of the app, the wording for that context might be different. To allow each screen to be very flexible and able to adapt, it's best to put it in the view itself
    @JsonClass(generateAdapter = true)
    data class ViewData(
        val loadingText: String
    )

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
                resetAppRunner.deleteAllAndReset()
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

        frag_main_loading.title = viewData.loadingText

        repos_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = ReposRecyclerViewAdapter(requireActivity())
            setHasFixedSize(true)
        }
    }

    override fun onStart() {
        super.onStart()

        reposViewModel.observeRepos()
            .observe(
                this,
                Observer { reposState ->
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
                            // We *should* be observing the repo list items but this version of Teller does not include the `.convert()` function which is needed to map the cache to the list item. So, below is a hack just to get the app to compile.
                            recyclerViewAdapter.items = cache.map { RepoListItem.Repo(it) }

                            frag_main_swapper.swapTo(SwapperViews.REPOS.name) {}
                        }

                        if (isFetching) {
                            fetchingSnackbar = Snackbar.make(parent_view, "Updating repos list...", Snackbar.LENGTH_LONG)
                            fetchingSnackbar?.show()
                        } else {
                            fetchingSnackbar?.dismiss()
                        }
                    }
                }
            )

        go_button.setOnClickListener {
            username_edittext.textIfValid?.let { username ->
                reposViewModel.setUsername(username.toString())
                closeKeyboard()
            }
        }
    }
}
