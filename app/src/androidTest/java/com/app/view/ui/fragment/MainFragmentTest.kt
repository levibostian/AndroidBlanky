package com.app.view.ui.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.R
import com.app.ScreenshotOnly
import com.app.di.DatabaseModule
import com.app.di.NetworkModule
import com.app.mock.MockWebServer
import com.app.service.model.RepoModel
import com.app.service.model.RepoOwnerModel
import com.app.util.EspressoTestUtil.assertListItems
import com.app.util.EspressoTestUtil.scrollToTopOfList
import com.app.util.TestSetupUtil
import com.app.view.FragmentEspressoTest
import com.app.view.ui.activity.FragmentTestingActivity
import com.google.common.truth.Truth.*
import com.levibostian.teller.testing.extensions.cache
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DatabaseModule::class, NetworkModule::class)
class MainFragmentTest : FragmentEspressoTest<MainFragment>() {

    override fun provideTestClass(): Any = this

    @Inject lateinit var mockWebServer: MockWebServer
    @Inject lateinit var testSetup: TestSetupUtil

    private val reposRecyclerViewId = R.id.repos_recyclerview
    private val usernameEditTextId = R.id.username_edittext
    private val goButtonId = R.id.go_button
    private val loadingViewId = R.id.frag_main_loading

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
    }

    @After
    fun teardown() {
        testSetup.teardown()
    }

    @Test
    fun programList_givenProgramsLoading_expectShowLoading() {
        launchFragment()

        onView(withId(usernameEditTextId))
            .perform(typeText("levi"))
        onView(withId(goButtonId))
            .perform(click())

        onView(withId(loadingViewId))
            .check(matches(isDisplayed()))
        onView(withId(reposRecyclerViewId))
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun programsList_givenPrograms_expectShowsInList() {
        val cache = listOf(
            RepoModel(1, "repo-1", RepoOwnerModel("levi"))
        )

        mockWebServer.queue(200, cache.toTypedArray())

        launchFragment()

        onView(withId(usernameEditTextId))
            .perform(typeText("levi"))
        onView(withId(goButtonId))
            .perform(click())

        onView(withId(loadingViewId))
            .check(matches(not(isDisplayed())))
        onView(withId(reposRecyclerViewId))
            .check(matches(isDisplayed()))

        assertListItems(reposRecyclerViewId, cache) { index, item ->
            matches(hasDescendant(withText(item.name)))
        }
    }

    @Test
    @ScreenshotOnly
    fun screenshot_programs_list() {
        val cache = listOf(
            RepoModel(1, "repo-1", RepoOwnerModel("levi"))
        )

        mockWebServer.queue(200, cache.toTypedArray())

        launchFragment()

        onView(withId(usernameEditTextId))
            .perform(typeText("levi"))
        onView(withId(goButtonId))
            .perform(click())

        onView(withId(loadingViewId))
            .check(matches(not(isDisplayed())))
        onView(withId(reposRecyclerViewId))
            .check(matches(isDisplayed()))

        assertListItems(reposRecyclerViewId, cache) { index, item ->
            matches(hasDescendant(withText(item.name)))
        }

        scrollToTopOfList(reposRecyclerViewId)

        screenshots.take("Repos List")
    }
}
