package com.app.view

import android.app.Instrumentation
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.app.di.TestAppGraph
import com.app.util.EspressoTestUtil
import com.app.util.ScreenshotUtil
import com.app.view.ui.TestMainApplication
import com.app.view.ui.activity.FragmentTestingActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import tools.fastlane.screengrab.locale.LocaleTestRule

/**
 * Base class for running Espresso tests.
 *
 * This class does the following for you:
 * 1. Creates test rules that are very common to use.
 * 2. Makes some convenient assumptions to make running tests quick/easy. Example: If you want to test a Fragment, you only need to override 1 function and the rest is done for you. But this class is also expandable and flexible so you're not locked into doing things certain ways. Feel free to override functions below to make it your own.
 */
abstract class FragmentEspressoTest<Frag : Fragment> {

    val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    val context = instrumentation.targetContext
    val application: TestMainApplication = instrumentation.targetContext.applicationContext as TestMainApplication

    @get:Rule val localeTestRule = LocaleTestRule() // fastlane can switch locales to take screenshots and test.
    // @get:Rule val screenshotOnErrorRule = ScreenshotOnErrorRule.getRule()
    @get:Rule val mockitoTestRule: MockitoRule = MockitoJUnit.rule()
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val diGraph: TestAppGraph
        get() = application.appComponent as TestAppGraph

    protected lateinit var activityScenario: ActivityScenario<FragmentTestingActivity>

    protected lateinit var screenshots: ScreenshotUtil

    abstract fun constructFragment(): Frag

    /**
     * Optional override if you want to perform anything after.
     */
    open fun afterFragmentLaunch(activity: FragmentTestingActivity) {
    }

    @Before
    open fun setup() {
        activityScenario = launchActivity()
        activityScenario.onActivity { activity ->
            this.screenshots = ScreenshotUtil(activity)
            EspressoTestUtil.disableAnimations(activity)
        }
    }

    @After
    fun cleanup() {
        activityScenario.close()
    }

    protected fun launchFragment() {
        activityScenario.onActivity { activity ->
            activity.showFragment(constructFragment())

            afterFragmentLaunch(activity)
        }
    }
}
