package com.levibostian.androidblanky.view.fragment

import android.app.Instrumentation
import android.support.test.filters.SdkSuppress
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.levibostian.androidblanky.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import tools.fastlane.screengrab.locale.LocaleTestRule
import tools.fastlane.screengrab.Screengrab
import javax.inject.Inject
import org.mockito.Mockito.*
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.model.RepoOwnerModel
import com.levibostian.androidblanky.view.ui.TestMainApplication
import org.mockito.*
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.levibostian.androidblanky.rule.ScreenshotOnErrorRule
import com.levibostian.androidblanky.testing.SingleFragmentActivity
import com.levibostian.androidblanky.util.ScreenshotUtil
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.TestViewModelFactory
import com.levibostian.teller.datastate.LocalDataState
import com.levibostian.teller.datastate.OnlineDataState
import com.squareup.spoon.SpoonRule
import org.hamcrest.CoreMatchers.not
import java.util.*

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainFragmentTest : AndroidIntegrationTestClass {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Mock private lateinit var gitHubUsernameViewModel: GitHubUsernameViewModel
    @Mock private lateinit var reposViewModel: ReposViewModel

    @get:Rule val activityRule = ActivityTestRule(SingleFragmentActivity::class.java, true, true)
    @get:Rule val localeTestRule = LocaleTestRule() // fastlane can switch locales to take screenshots and test.
    @get:Rule val spoon = SpoonRule()
    @get:Rule val screenshotOnErrorRule = ScreenshotOnErrorRule.getRule()

    private val repoOwner = RepoOwnerModel("login1")
    private val repo1 = RepoModel(1, "name1", repoOwner)
    private val repo2 = RepoModel(2, "name2", repoOwner)
    private val repo3 = RepoModel(3, "name3", repoOwner)

    private val reposLiveData: MutableLiveData<OnlineDataState<List<RepoModel>>> = MutableLiveData()
    private val githubUsernameLiveData: MutableLiveData<LocalDataState<String>> = MutableLiveData()

    private lateinit var application: TestMainApplication

    private lateinit var screenshots: ScreenshotUtil

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        application = instrumentation.targetContext.applicationContext as TestMainApplication
        (application.component as MockApplicationComponent).inject(this)

        (viewModelFactory as TestViewModelFactory).models = listOf(gitHubUsernameViewModel, reposViewModel)

        `when`(reposViewModel.observeRepos()).thenReturn(reposLiveData)
        `when`(gitHubUsernameViewModel.observeUsername()).thenReturn(githubUsernameLiveData)

        screenshots = ScreenshotUtil(activityRule.activity, spoon)
    }

    override fun getInstrumentation(): Instrumentation = InstrumentationRegistry.getInstrumentation()

    private fun launchActivity() {
        activityRule.activity.setFragment(MainFragment.newInstance())
    }

    @Test
    fun test_showReposDataListFetchingFresh() {
        val githubUsername = repo1.owner.name

        reposLiveData.postValue(OnlineDataState.data(listOf(repo1), Date()))
        githubUsernameLiveData.postValue(LocalDataState.data(githubUsername))

        launchActivity()

        orientationPortrait()

        screenshots.take("showReposDataListFetchingFresh")
        onView(withId(R.id.username_edittext))
                .check(ViewAssertions.matches(ViewMatchers.withText(githubUsername)))

        onView(withText(repo1.name))
                .check(ViewAssertions.matches(isDisplayed()))
    }

}