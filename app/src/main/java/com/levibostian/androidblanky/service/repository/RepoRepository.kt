package com.levibostian.androidblanky.service.repository

import com.levibostian.androidblanky.service.StateDataCompoundBehaviorSubject
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.statedata.StateData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.realm.RealmResults
import khronos.minutes

open class RepoRepository(private val reposDataSource: ReposDataSource,
                          private val gitHubUsernameDataSource: GitHubUsernameDataSource,
                          private val composite: CompositeDisposable): Repository {

    private val reposObservable: StateDataCompoundBehaviorSubject<RealmResults<RepoModel>> = StateDataCompoundBehaviorSubject()

    override fun observe() {
        if (reposObservable.hasValue()) return

        composite += reposDataSource.getData()
                .filter { !gitHubUsernameDataSource.getData().blockingFirst().isEmpty() }
                .filter {
                    val hasEverFetchedData = reposDataSource.hasEverFetchedData()
                    if (!hasEverFetchedData) {
                        reposObservable.onNextIsLoading()
                        sync().subscribe({
                        }, { error -> reposObservable.onNextCompoundError(error) })
                    }
                    hasEverFetchedData
                }
                .subscribe({ repos ->
                    val needToFetchFreshData = reposDataSource.isDataOlderThan(5.minutes.ago)

                    if (repos.isEmpty()) reposObservable.onNextEmpty(isFetchingFreshData = needToFetchFreshData)
                    else reposObservable.onNextData(repos, isFetchingFreshData = needToFetchFreshData)

                    if (needToFetchFreshData) {
                        sync().subscribe({
                            reposObservable.onNextCompoundDoneFetchingFreshData()
                        }, { error -> reposObservable.onNextCompoundError(error) })
                    }
                })
    }

    override fun sync(): Completable {
        return gitHubUsernameDataSource.getData()
                .firstElement()
                .flatMapCompletable { username ->
                    if (username.isEmpty()) Completable.complete()
                    else reposDataSource.fetchNewData(ReposDataSource.FetchNewDataRequirements(username))
                }
    }

    @Suppress("UNCHECKED_CAST")
    fun getRepos(): Observable<StateData<RealmResults<RepoModel>>> {
        observe()
        return reposObservable.asObservable()
    }

    fun setUserToGetReposFor(gitHubUsername: String): Completable {
        observe()
        return Completable.concatArray(gitHubUsernameDataSource.saveData(gitHubUsername), reposDataSource.resetData())
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