package com.levibostian.androidblanky.view.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.GitHubService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.arch.lifecycle.ViewModelProviders
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.statedata.StateData
import com.levibostian.androidblanky.view.ui.LifecycleCompositeDisposable
import com.levibostian.androidblanky.view.ui.plusAssign
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import com.levibostian.androidblanky.viewmodel.ViewModelProviderWrapper
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.toSingle
import io.realm.RealmResults

class MainFragment : SupportFragmentLifecycle() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private val lifecycleComposite = LifecycleCompositeDisposable.init(this)

    private lateinit var reposViewModel: ReposViewModel

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

        (activity.application as MainApplication).component.inject(this)
        reposViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReposViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()

        lifecycleComposite += reposViewModel.getReposUsername()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ username ->
                    fragment_main_username_textview.text = if (username.isBlank()) "(no username set)" else username
                })
        lifecycleComposite += reposViewModel.getRepos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ reposState ->
                    when (reposState.state) {
                        StateData.State.LOADING -> TODO()
                        StateData.State.EMPTY -> {}
                        StateData.State.ERROR -> {}
                        StateData.State.DATA -> TODO()
                    }
                })
    }

}
