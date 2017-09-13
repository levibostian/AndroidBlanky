package com.levibostian.androidblanky.service.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.model.OwnerModel
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.statedata.StateData
import com.nhaarman.mockito_kotlin.times
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.realm.RealmResults
import khronos.Dates
import khronos.minus
import khronos.minutes
import khronos.second
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import org.mockito.ArgumentCaptor



@RunWith(MockitoJUnitRunner::class)
class RepoRespositoryTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var reposDataSource: ReposDataSource
    @Mock private lateinit var githubUsernameDataSource: GitHubUsernameDataSource
    @Mock private lateinit var composite: CompositeDisposable
    @Mock private lateinit var repos: RealmResults<RepoModel>
    @Mock private lateinit var completable: Completable
    @Mock private lateinit var maybeString: Maybe<String>

    @Captor private lateinit var maybeObservableStringArgumentCaptor: ArgumentCaptor<MaybeObserver<in String>>

    private lateinit var repository: RepoRepository

    val repo1 = RepoModel("name1", "desc1", OwnerModel("login1"))
    val repo2 = RepoModel("name2", "desc2", OwnerModel("login2"))
    val repo3 = RepoModel("name3", "desc3", OwnerModel("login3"))

    @Before fun setUp() {
        `when`(reposDataSource.getData()).thenReturn(Observable.never())
        repository = RepoRepository(reposDataSource, githubUsernameDataSource, composite)
    }

    @Test fun getRepos_getEmptyStateDefault() {
        `when`(reposDataSource.lastTimeNewDataFetched()).thenReturn(Dates.today.minus(1.second))

        repository.getRepos()
                .test()
                .assertNotComplete()
                .assertValue {
                    it.state == StateData.State.EMPTY
                }
    }

    @Test fun getRepos_getLoadingStateWhenSetNewUsernameToGetReposFor() {
        `when`(reposDataSource.lastTimeNewDataFetched()).thenReturn(Dates.today.minus(1.second))
        `when`(githubUsernameDataSource.saveData(ArgumentMatchers.anyString())).thenReturn(Completable.complete())
        `when`(reposDataSource.fetchNewData(com.nhaarman.mockito_kotlin.any())).thenReturn(Completable.never())

        repository.setUserToGetReposFor("username")
                .test()
                .assertComplete()

        repository.getRepos()
                .test()
                .assertValue {
                    it.state == StateData.State.LOADING && it.repos == null && it.error == null
                }
    }

    @Test fun getRepos_errorState() {
        `when`(reposDataSource.lastTimeNewDataFetched()).thenReturn(Dates.today.minus(1.second))
        `when`(reposDataSource.getData()).thenReturn(Observable.error(IOException("Some error")))
        repository = RepoRepository(reposDataSource, githubUsernameDataSource, composite)

        repository.getRepos()
                .test()
                .assertNotComplete()
                .assertValue {
                    it.state == StateData.State.ERROR &&
                    it.repos == null &&
                    it.error != null && it.error is IOException
                }
    }

    @Test fun getRepos_emptyState() {
        //val reposArray = arrayListOf(repo1, repo2)
        //`when`(repos.iterator()).thenReturn(reposArray.iterator())
        //`when`(repos.size).thenReturn(reposArray.size)

        `when`(reposDataSource.getData()).thenReturn(Observable.fromArray(repos))
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.just(""))
        repository = RepoRepository(reposDataSource, githubUsernameDataSource, composite)

        verify(githubUsernameDataSource).getData()

        repository.getRepos()
                .test()
                .assertNotComplete()
                .assertValue {
                    it.state == StateData.State.EMPTY && it.repos == null && it.error == null
                }
    }

    @Test fun getRepos_emptyStateWithUsernamePresent() {
        //val reposArray = arrayListOf<RepoModel>()
        //`when`(repos.iterator()).thenReturn(reposArray.iterator())
        //`when`(repos.size).thenReturn(reposArray.size)
        `when`(repos.isEmpty()).thenReturn(true)

        `when`(reposDataSource.getData()).thenReturn(Observable.create { it.onNext(repos) })
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.just("username"))
        repository = RepoRepository(reposDataSource, githubUsernameDataSource, composite)

        verify(githubUsernameDataSource).getData()

        repository.getRepos()
                .test()
                .assertNotComplete()
                .assertValue {
                    it.state == StateData.State.EMPTY && it.repos == null && it.error == null
                }
    }

    @Test fun getRepos_dataState() {
        val reposArray = arrayListOf(repo1, repo2)
        //`when`(repos.iterator()).thenReturn(reposArray.iterator())
        `when`(repos.size).thenReturn(reposArray.size)

        `when`(reposDataSource.getData()).thenReturn(Observable.fromArray(repos))
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.just("username"))
        repository = RepoRepository(reposDataSource, githubUsernameDataSource, composite)

        verify(githubUsernameDataSource).getData()

        repository.getRepos()
                .test()
                .assertNotComplete()
                .assertValue {
                    it.state == StateData.State.DATA &&
                            it.repos!!.size == 2 &&
                            it.error == null
                }
    }

    @Test fun getRepos_doNotFetchNewDataHasNotBeenLongEnoughTimeSinceLastFetch() {
        `when`(reposDataSource.lastTimeNewDataFetched()).thenReturn(Dates.today.minus(1.minutes))

        repository.getRepos()
                .test()

        verify(reposDataSource, never()).fetchNewData(com.nhaarman.mockito_kotlin.any())
    }

    @Test fun getRepos_fetchNewDataLastFetchTimeNotSet() {
        `when`(reposDataSource.lastTimeNewDataFetched()).thenReturn(null)
        `when`(reposDataSource.fetchNewData(com.nhaarman.mockito_kotlin.any())).thenReturn(completable)
        `when`(completable.toMaybe<String>()).thenReturn(maybeString)
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.just("username"))

        repository.getRepos()
                .test()

        verify(reposDataSource).fetchNewData(com.nhaarman.mockito_kotlin.any())
        verify(maybeString).subscribe(maybeObservableStringArgumentCaptor.capture())
        Truth.assertThat(maybeObservableStringArgumentCaptor.value).isNotNull()
    }

    @Test fun getRepos_fetchNewDataLastFetchTimeWhileAgo() {
        `when`(reposDataSource.lastTimeNewDataFetched()).thenReturn(Dates.today.minus(5.minutes).minus(1.second))
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.just("username"))

        repository.getRepos()
                .test()

        verify(reposDataSource).fetchNewData(com.nhaarman.mockito_kotlin.any())
    }

    @Test fun getReposUsername_getDefaultEmptyString() {
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.create { it.onNext("") })

        repository.getReposUsername()
                .test()
                .assertNotComplete()
                .assertValue { it == "" }
    }

    @Test fun getReposUsername_getUsername() {
        `when`(githubUsernameDataSource.getData()).thenReturn(Observable.create { it.onNext("username") })

        repository.getReposUsername()
                .test()
                .assertNotComplete()
                .assertValue { it == "username" }
    }

    @Test fun cleanup_clearAndCleanup() {
        repository.cleanup()

        verify(composite, times(1)).clear()
        verify(reposDataSource, times(1)).cleanup()
        verify(githubUsernameDataSource, times(1)).cleanup()
    }

}