package com.levibostian.androidblanky.view.fragment

import android.app.Instrumentation
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.levibostian.androidblanky.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import tools.fastlane.screengrab.locale.LocaleTestRule
import javax.inject.Inject
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.model.RepoOwnerModel
import com.levibostian.androidblanky.view.ui.TestMainApplication
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.levibostian.androidblanky.mock.MockWebServer
import com.levibostian.androidblanky.rule.BackgroundTasksDrainRule
import com.levibostian.androidblanky.rule.DiGraphRule
import com.levibostian.androidblanky.service.AppService
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.service.vo.request.UpdateFcmTokenRequestBody
import com.levibostian.androidblanky.service.vo.response.MessageResponseVo
import com.levibostian.androidblanky.util.EspressoTestUtil
import com.levibostian.androidblanky.util.ScreenshotUtil
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.teller.cachestate.LocalCacheState
import com.levibostian.teller.cachestate.OnlineCacheState
import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.testing.extensions.cache
import com.levibostian.teller.testing.extensions.initState
import com.levibostian.teller.testing.extensions.initStateAsync
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainFragmentTest {

    @Inject lateinit var reposRepository: ReposRepository
    @Inject lateinit var githubUsernameRepository: GitHubUsernameRepository
    @Inject lateinit var mockWebServer: MockWebServer
    @Inject lateinit var apiService: AppService

    @get:Rule val localeTestRule = LocaleTestRule() // fastlane can switch locales to take screenshots and test.
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val diGraph = DiGraphRule()

    private val repoOwner = RepoOwnerModel("login1")
    private val repo1 = RepoModel(1, "name1", repoOwner)
    private val repo2 = RepoModel(2, "name2", repoOwner)
    private val repo3 = RepoModel(3, "name3", repoOwner)

    private lateinit var screenshots: ScreenshotUtil

    @Before
    fun setup() {
        diGraph.graph.inject(this)
    }

    private fun launchFragment() {
        val scenario = launchFragmentInContainer {
            MainFragment.newInstance()
        }
        scenario.onFragment { frag ->
            EspressoTestUtil.disableProgressBarAnimations(frag.activity!!)
            screenshots = ScreenshotUtil(frag.activity!!)
        }
    }

    @Test
    fun test_showReposDataListFetchingFresh() {
        val githubUsername = repo1.owner.name
        val requirements = ReposRepository.GetRequirements(githubUsername)

        runBlocking {
            OnlineRepository.Testing.initState(reposRepository, requirements) {
                cache(listOf(repo1))
            }

            githubUsernameRepository.saveCache(githubUsername, GitHubUsernameRepository.Requirements())
        }

        launchFragment()

        screenshots.take("showReposDataListFetchingFresh")
        onView(withId(R.id.username_edittext))
                .check(matches(withText(githubUsername)))

        onView(withText(repo1.name))
                .check(matches(isDisplayed()))
    }

    @Test
    fun test_networking() {
        mockWebServer.queue(200, MessageResponseVo("message here"))

        val message = apiService.updateFcmToken(UpdateFcmTokenRequestBody("")).blockingGet().response()!!.body()!!.message

        Truth.assertThat(message).isEqualTo("message here")
    }

}