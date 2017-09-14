package com.levibostian.androidblanky.service.repository

import com.levibostian.androidblanky.service.statedata.StateData
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.statedata.ReposStateData
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.RealmResults
import khronos.Dates
import khronos.minus
import khronos.minutes
import khronos.plus

open class RepoRepository(private val reposDataSource: ReposDataSource,
                          private val gitHubUsernameDataSource: GitHubUsernameDataSource,
                          private val composite: CompositeDisposable): Repository {

    private val getReposObservable: BehaviorSubject<ReposStateData> = BehaviorSubject.create()

    init {
        getReposObservable.onNext(ReposStateData.empty())

        composite += reposDataSource.getData()
                .subscribe({ repos ->
                    if (gitHubUsernameDataSource.getData().blockingFirst().isBlank()) {
                        getReposObservable.onNext(ReposStateData.empty())
                    } else {
                        getReposObservable.onNext(if (repos.isEmpty()) ReposStateData.empty() else ReposStateData.success(repos))
                    }
                }, { error ->
                    getReposObservable.onNext(ReposStateData.error(error))
                })
    }

    @Suppress("UNCHECKED_CAST")
    fun getRepos(): Observable<ReposStateData> {
        return (getReposObservable as Observable<ReposStateData>)
                .doOnSubscribe {
                    val lastFetch = reposDataSource.lastTimeNewDataFetched()

                    if (lastFetch == null || lastFetch < Dates.today.minus(5.minutes)) {
                        gitHubUsernameDataSource.getData()
                                .firstElement()
                                .flatMap { username ->
                                    getReposObservable.onNext(ReposStateData.loading())
                                    reposDataSource.fetchNewData(ReposDataSource.FetchNewDataRequirements(username))
                                            .toMaybe<String>()
                                }
                                .subscribe()
                    }
                }
    }

    fun setUserToGetReposFor(gitHubUsername: String): Completable {
        return gitHubUsernameDataSource.saveData(gitHubUsername)
                .doOnComplete {
                    getReposObservable.onNext(ReposStateData.loading())
                    reposDataSource.fetchNewData(ReposDataSource.FetchNewDataRequirements(gitHubUsername))
                            .subscribe()
                }
    }

    fun getReposUsername(): Observable<String> {
        return gitHubUsernameDataSource.getData()
    }

    override fun cleanup() {
        composite.clear()

        reposDataSource.cleanup()
        gitHubUsernameDataSource.cleanup()
    }

}