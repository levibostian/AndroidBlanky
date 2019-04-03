package com.levibostian.androidblanky.view.fragment

import android.app.Instrumentation
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
import androidx.test.runner.AndroidJUnit4
import com.levibostian.androidblanky.rule.ScreenshotOnErrorRule
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.util.EspressoTestUtil
import com.levibostian.androidblanky.util.ScreenshotUtil
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.TestViewModelFactory
import com.levibostian.teller.cachestate.LocalCacheState
import com.levibostian.teller.cachestate.OnlineCacheState
import com.levibostian.teller.testing.extensions.cache
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.not
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainFragmentTest: AndroidIntegrationTestClass {

    @Mock private lateinit var gitHubUsernameViewModel: GitHubUsernameViewModel
    @Mock private lateinit var reposViewModel: ReposViewModel
    @Mock private lateinit var reposRequirements: ReposRepository.GetRequirements
    @Mock private lateinit var githubUsernameRequirements: GitHubUsernameRepository.Requirements

    @get:Rule val localeTestRule = LocaleTestRule() // fastlane can switch locales to take screenshots and test.
    @get:Rule val screenshotOnErrorRule = ScreenshotOnErrorRule.getRule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repoOwner = RepoOwnerModel("login1")
    private val repo1 = RepoModel(1, "name1", repoOwner)
    private val repo2 = RepoModel(2, "name2", repoOwner)
    private val repo3 = RepoModel(3, "name3", repoOwner)

    private val reposLiveData: MutableLiveData<OnlineCacheState<List<RepoModel>>> = MutableLiveData()
    private val githubUsernameLiveData: MutableLiveData<LocalCacheState<String>> = MutableLiveData()

    private lateinit var application: TestMainApplication

    private lateinit var screenshots: ScreenshotUtil

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        application = instrumentation.targetContext.applicationContext as TestMainApplication
        (application.component as MockApplicationComponent).inject(this)

        (viewModelFactory as TestViewModelFactory).models = listOf(gitHubUsernameViewModel, reposViewModel)

        whenever(reposViewModel.observeRepos()).thenReturn(reposLiveData)
        whenever(gitHubUsernameViewModel.observeUsername()).thenReturn(githubUsernameLiveData)

        screenshots = ScreenshotUtil()
    }

    private fun launchActivity() {
        val scenario = launchFragmentInContainer {
            MainFragment.newInstance()
        }
        scenario.onFragment {
            EspressoTestUtil.disableProgressBarAnimations(it.activity!!)
        }
    }

    @Test
    fun test_showReposDataListFetchingFresh() {
        val githubUsername = repo1.owner.name

        launchActivity()

        reposLiveData.postValue(OnlineCacheState.Testing.cache(reposRequirements, Date()) {
            cache(listOf(repo1))
        })
        githubUsernameLiveData.postValue(LocalCacheState.Testing.cache(githubUsernameRequirements, githubUsername))

        screenshots.take("showReposDataListFetchingFresh")
        onView(withId(R.id.username_edittext))
                .check(matches(withText(githubUsername)))

        onView(withText(repo1.name))
                .check(matches(not(isDisplayed())))
    }

}