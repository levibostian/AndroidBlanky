package com.levibostian.androidblanky.view.fragment

import android.app.Instrumentation
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.SdkSuppress
import com.levibostian.androidblanky.view.ui.activity.MainActivity
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import android.support.test.runner.AndroidJUnit4
import com.f2prateek.rx.preferences2.Preference
import com.levibostian.androidblanky.*
import com.levibostian.androidblanky.service.GitHubService
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import tools.fastlane.screengrab.locale.LocaleTestRule
import tools.fastlane.screengrab.Screengrab
import javax.inject.Inject
import org.mockito.Mockito.*
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.model.OwnerModel
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import com.levibostian.androidblanky.view.ui.TestMainApplication
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import khronos.Dates
import khronos.minutes
import org.mockito.*
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
open class MainFragmentTest : AndroidIntegrationTestClass {

    @Inject lateinit var gitHubService: GitHubService
    @Inject lateinit var sharedPrefs: SharedPreferences
    @Inject lateinit var rxSharedPrefsWrapper: RxSharedPreferencesWrapper
    @Inject lateinit var realmInstanceManager: RealmInstanceManager

    @Mock private lateinit var preference: Preference<String>
    @Mock private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    @get:Rule open val main = ActivityTestRule(MainActivity::class.java, false, false)
    @get:Rule open val localeTestRule = LocaleTestRule() // fastlane can switch locales to take screenshots and test.

    private val repo1 = RepoModel("name1", "description1", OwnerModel("login1"))
    private val repo2 = RepoModel("name2", "description2", OwnerModel("login2"))
    private val repo3 = RepoModel("name3", "description3", OwnerModel("login3"))

    private lateinit var application: TestMainApplication

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        application = instrumentation.targetContext.applicationContext as TestMainApplication
        (application.component as MockApplicationComponent).inject(this)

        RealmInstanceManager.getInMemory().executeTransaction { realm -> realm.deleteAll()}

        instrumentation.runOnMainSync { // Make sure to run this on the instrumentation UI thread or Realm will give you an error saying that "you cannot close a realm instance from another thread that created it" even though my app code is calling from the UI thread. This asserts the Realm instances are created on the UI thread.
            `when`(realmInstanceManager.getDefault()).thenAnswer { RealmInstanceManager.getInMemory() }
            `when`(sharedPrefs.edit()).thenReturn(sharedPrefsEditor)
        }
    }

    override fun getInstrumentation(): Instrumentation = InstrumentationRegistry.getInstrumentation()

    private fun launchActivity() {
        main.launchActivity(null)
    }

    @Test fun showReposDataList() {
        RealmInstanceManager.getInMemory().executeTransaction { realm ->
            realm.insertOrUpdate(listOf(repo1, repo2, repo3))
        }

        `when`(sharedPrefs.getLong(SharedPrefersKeys.lastTimeReposFetchedKey, 0)).thenReturn(Dates.today.time)
        `when`(rxSharedPrefsWrapper.getString(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.gitHubUsernameKey))).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.create { it.onNext("levibostian") })
        `when`(gitHubService.getRepos(com.nhaarman.mockito_kotlin.eq("levibostian"))).thenReturn(Result.response(Response.success(listOf(repo1, repo2, repo3))).toSingle())
        `when`(sharedPrefsEditor.putLong(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.lastTimeReposFetchedKey), ArgumentMatchers.anyLong())).thenReturn(sharedPrefsEditor)

        launchActivity()

        orientationPortrait()

        Screengrab.screenshot("showReposDataList")
        onView(withId(R.id.fragment_main_username_textview))
                .check(ViewAssertions.matches(ViewMatchers.withText("levibostian")))

        onView(withText(repo1.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(repo2.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(repo3.full_name)).check(ViewAssertions.matches(isDisplayed()))

        verify(sharedPrefsEditor, com.nhaarman.mockito_kotlin.never()).commit()
    }

    @Test fun showReposDataListOrientationChange() {
        RealmInstanceManager.getInMemory().executeTransaction { realm ->
            realm.insertOrUpdate(listOf(repo1, repo2, repo3))
        }

        `when`(sharedPrefs.getLong(SharedPrefersKeys.lastTimeReposFetchedKey, 0)).thenReturn(Dates.today.time)
        `when`(rxSharedPrefsWrapper.getString(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.gitHubUsernameKey))).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.create { it.onNext("levibostian") })
        `when`(gitHubService.getRepos(com.nhaarman.mockito_kotlin.eq("levibostian"))).thenReturn(Result.response(Response.success(listOf(repo1, repo2, repo3))).toSingle())
        `when`(sharedPrefsEditor.putLong(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.lastTimeReposFetchedKey), ArgumentMatchers.anyLong())).thenReturn(sharedPrefsEditor)

        launchActivity()

        orientationPortrait()

        onView(withId(R.id.fragment_main_username_textview))
                .check(ViewAssertions.matches(ViewMatchers.withText("levibostian")))

        onView(withText(repo1.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(repo2.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(repo3.full_name)).check(ViewAssertions.matches(isDisplayed()))

        verify(sharedPrefsEditor, com.nhaarman.mockito_kotlin.never()).commit()

        orientationLandscape()

        Screengrab.screenshot("showReposDataListOrientationChange")
        onView(withId(R.id.fragment_main_username_textview))
                .check(ViewAssertions.matches(ViewMatchers.withText("levibostian")))

        onView(withText(repo1.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(repo2.full_name)).check(ViewAssertions.matches(isDisplayed()))
        onView(withText(repo3.full_name)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test fun showEmptyList() {
        `when`(sharedPrefs.getLong(SharedPrefersKeys.lastTimeReposFetchedKey, 0)).thenReturn(10.minutes.ago.time)
        `when`(rxSharedPrefsWrapper.getString(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.gitHubUsernameKey))).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.create { it.onNext("levibostian") })
        `when`(gitHubService.getRepos(com.nhaarman.mockito_kotlin.eq("levibostian"))).thenReturn(Result.response(Response.success(listOf<RepoModel>())).toSingle())
        `when`(sharedPrefsEditor.putLong(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.lastTimeReposFetchedKey), ArgumentMatchers.anyLong())).thenReturn(sharedPrefsEditor)

        launchActivity()

        orientationPortrait()

        Screengrab.screenshot("showEmptyList")
        onView(withId(R.id.fragment_main_username_textview))
                .check(ViewAssertions.matches(ViewMatchers.withText("levibostian")))

        onView(withText(application.getString(R.string.user_no_repos))).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test fun showLoadingView() {
        `when`(sharedPrefs.getLong(SharedPrefersKeys.lastTimeReposFetchedKey, 0)).thenReturn(0)
        `when`(rxSharedPrefsWrapper.getString(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.gitHubUsernameKey))).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.create { it.onNext("levibostian") })
        `when`(gitHubService.getRepos(com.nhaarman.mockito_kotlin.eq("levibostian"))).thenReturn(Single.never())
        `when`(sharedPrefsEditor.putLong(com.nhaarman.mockito_kotlin.eq(SharedPrefersKeys.lastTimeReposFetchedKey), ArgumentMatchers.anyLong())).thenReturn(sharedPrefsEditor)

        launchActivity()

        orientationPortrait()

        Screengrab.screenshot("showLoadingView")
        onView(withId(R.id.fragment_main_username_textview))
                .check(ViewAssertions.matches(ViewMatchers.withText("levibostian")))

        onView(withText(application.getString(R.string.loading_repos))).check(ViewAssertions.matches(isDisplayed()))
    }

}