package com.levibostian.androidblanky.service.repository

import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.statedata.StateData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import khronos.minutes

open class RepoRepository(private val reposDataSource: ReposDataSource,
                          private val gitHubUsernameDataSource: GitHubUsernameDataSource,
                          composite: CompositeDisposable): Repository(composite) {

    override fun startObservingStateOfData() {
        startObservingStateOfData(reposDataSource, 5.minutes.ago, { repos -> repos.isEmpty() }, handleObservable = { getDataObservable ->
            getDataObservable.filter { !gitHubUsernameDataSource.getData().blockingFirst().isEmpty() }
        })
    }

    override fun sync(): Completable {
        return gitHubUsernameDataSource.getData()
                .firstElement()
                .flatMapCompletable { username ->
                    if (username.isEmpty()) Completable.complete()
                    else reposDataSource.fetchFreshData(ReposDataSource.FetchNewDataRequirements(username))
                }
    }

    @Suppress("UNCHECKED_CAST")
    fun getRepos(): Observable<StateData<RealmResults<RepoModel>>> {
        startObservingStateOfData()
        return getSubjectOrCreate(reposDataSource).asObservable()
    }

    fun setUserToGetReposFor(gitHubUsername: String): Completable {
        startObservingStateOfData()
        return Completable.concatArray(gitHubUsernameDataSource.saveData(gitHubUsername), reposDataSource.resetData())
    }

    fun getReposUsername(): Observable<String> {
        return gitHubUsernameDataSource.getData()
    }

    override fun cleanupResources() {
        reposDataSource.cleanup()
        gitHubUsernameDataSource.cleanup()
    }

}