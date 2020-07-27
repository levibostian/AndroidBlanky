package com.app.view.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.R
import com.app.ScreenshotOnly
import com.app.di.DatabaseModule
import com.app.service.datasource.ReposTellerRepository
import com.app.service.model.RepoModel
import com.app.service.model.RepoOwnerModel
import com.app.util.EspressoTestUtil.assertListItems
import com.app.util.EspressoTestUtil.scrollToTopOfList
import com.app.util.TestSetupUtil
import com.app.view.FragmentEspressoTest
import com.app.view.ui.activity.FragmentTestingActivity
import com.app.viewmodel.ReposViewModel
import com.google.common.truth.Truth.*
import com.levibostian.teller.cachestate.OnlineCacheState
import com.levibostian.teller.testing.extensions.cache
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DatabaseModule::class)
class MainFragmentTest : FragmentEspressoTest<MainFragment>() {

    override fun provideTestClass(): Any = this

    @Mock lateinit var reposViewModel: ReposViewModel

    @Inject lateinit var testSetup: TestSetupUtil

    private val reposListItemsLiveData: MutableLiveData<OnlineCacheState<List<RepoModel>>> = MutableLiveData()

    private val reposRecyclerViewId = R.id.repos_recyclerview

    override fun constructFragment(): MainFragment {
        return MainFragment()
    }

    override fun afterFragmentLaunch(activity: FragmentTestingActivity) {
        super.afterFragmentLaunch(activity)

        activity.showToolbar(context.getString(R.string.repos))
    }

    @Before
    override fun setup() {
        super.setup()

        diRule.inject()
        testSetup.setup()

        whenever(reposViewModel.observeRepos()).thenReturn(reposListItemsLiveData)
    }

    @Test
    fun programsList_givenPrograms_expectShowsInList() {
        launchFragment()

        val cache = mutableListOf<RepoModel>().apply {
            add(RepoModel(1, "repo-1", RepoOwnerModel("")))
        }

        reposListItemsLiveData.postValue(
            OnlineCacheState.Testing.cache(ReposTellerRepository.GetRequirements(""), Date()) {
                cache(cache)
            }
        )

        assertListItems(reposRecyclerViewId, cache) { index, item ->
            matches(hasDescendant(withText(item.name)))
        }
    }

    @Test
    @ScreenshotOnly
    fun screenshot_programs_list() {
        programsList_givenPrograms_expectShowsInList()

        scrollToTopOfList(reposRecyclerViewId)

        screenshots.takeForStore("Repos List")
    }
}
