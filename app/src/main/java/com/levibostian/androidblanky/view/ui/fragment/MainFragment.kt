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
import com.levibostian.androidblanky.service.rx.HttpDisposableObserver
import com.levibostian.androidblanky.service.rx.HttpErrorMessageHandler
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.vo.RepoVo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainFragment : Fragment(), HttpErrorMessageHandler {

    @Inject lateinit var service: GitHubService
    private val composite = CompositeDisposable()

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

        MainApplication.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()

        val isUsernameNotBlank = RxTextView.afterTextChangeEvents(fragment_main_github_username)
                .map { fragment_main_github_username.text.isNotBlank() }
                .share()
        composite += isUsernameNotBlank.subscribe(RxView.enabled(fragment_main_find_num_repos))
        composite += isUsernameNotBlank
                .filter { usernameNotBlank -> !usernameNotBlank }
                .map { _ -> "Enter a username." }
                .subscribe(RxTextView.error(fragment_main_github_username))

        composite += RxView.clicks(fragment_main_find_num_repos)
                .map { RxTextView.text(fragment_main_num_repos_textview).accept("Loading...") }
                .debounce(500, TimeUnit.MILLISECONDS)
                .flatMap {
                    service.listRepos(fragment_main_github_username.text.toString())
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : HttpDisposableObserver<List<RepoVo>>(MainFragment@this) {
                    override fun onNext(repos: List<RepoVo>) {
                        fragment_main_num_repos_textview.text = "Number of repos: ${repos.count()}"
                    }
                    override fun onError(e: Throwable) {
                        fragment_main_num_repos_textview.text = "Error encountered. ${getErrorMessage(e)}"
                    }
                })
    }

    override fun onStop() {
        composite.dispose()
        super.onStop()
    }

    override fun handleHttpErrorMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

}
