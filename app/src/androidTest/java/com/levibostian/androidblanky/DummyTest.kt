package com.levibostian.androidblanky

import com.levibostian.androidblanky.view.ui.activity.MainActivity
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import android.support.test.runner.AndroidJUnit4
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.MockGitHubService
import com.levibostian.androidblanky.service.module.DataSourceModule
import com.levibostian.androidblanky.service.module.RepositoryModule
import com.levibostian.androidblanky.service.module.ServiceModule
import com.levibostian.androidblanky.view.ui.ApplicationComponent
import com.levibostian.androidblanky.view.ui.DaggerApplicationComponent
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.viewmodel.module.ManagerModule
import com.levibostian.androidblanky.viewmodel.module.ViewModelModule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.`is`
import org.junit.Before
import retrofit2.Retrofit
import tools.fastlane.screengrab.locale.LocaleTestRule
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy

@RunWith(AndroidJUnit4::class)
open class DummyTest {

    @get:Rule open val main = ActivityTestRule(MainActivity::class.java)
    @get:Rule open val localeTestRule = LocaleTestRule()

    private lateinit var application: MainApplication

    @Before
    fun setup() {
        Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())

        application = main.activity.applicationContext as MainApplication

        MainApplication.component

        val component = DaggerApplicationComponent.builder()
                .serviceModule(object : ServiceModule(application) {
                    override fun provideService(retrofit: Retrofit): GitHubService {
                        return MockGitHubService()
                    }
                })
                .managerModule(ManagerModule(application))
                .repositoryModule(RepositoryModule(application))
                .viewModelModule(ViewModelModule(application))
                .dataSourceModule(DataSourceModule(application))
                .build()

        MainApplication.component = component
    }

    @Test fun noneOfTheThings() {
        Screengrab.screenshot("initial_state")
        Thread.sleep(3000)
        assertThat(true, `is`(true))
        Screengrab.screenshot("data_state")
    }

}