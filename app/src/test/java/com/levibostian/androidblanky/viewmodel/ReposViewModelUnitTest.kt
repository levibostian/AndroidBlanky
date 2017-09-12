package com.levibostian.androidblanky.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.levibostian.androidblanky.service.model.OwnerModel
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.service.statedata.ReposStateData
import com.levibostian.androidblanky.service.statedata.StateData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import rx.subjects.BehaviorSubject

@RunWith(MockitoJUnitRunner::class)
class ReposViewModelUnitTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var repository: RepoRepository

    @Captor private lateinit var userArgumentCaptor: ArgumentCaptor<RepoModel>

    private lateinit var viewModel: ReposViewModel

    @Before fun setUp() {
        viewModel = ReposViewModel(repository)
    }

    @Test fun getRepos_whenNoReposSaved() {
        `when`(repository.getRepos()).thenReturn(Observable.empty<ReposStateData>())

        viewModel.getRepos()
                .test()
                .assertNoValues()
    }

    @Test fun onCleared_everythingCleared() {
        viewModel.cleanup()

        verify(repository, times(1)).cleanup()
    }

}

