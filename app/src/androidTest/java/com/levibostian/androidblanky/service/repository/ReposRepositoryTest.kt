package com.levibostian.androidblanky.service.repository

import android.app.Instrumentation
import android.content.SharedPreferences
import org.hamcrest.Matchers.not
import com.levibostian.androidblanky.view.ui.activity.MainActivity
import org.junit.Rule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.f2prateek.rx.preferences2.Preference
import com.levibostian.androidblanky.*
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.Database
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Before
import tools.fastlane.screengrab.locale.LocaleTestRule
import tools.fastlane.screengrab.Screengrab
import javax.inject.Inject
import org.mockito.Mockito.*
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.model.RepoOwnerModel
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import com.levibostian.androidblanky.view.ui.TestMainApplication
import io.reactivex.Single
import org.mockito.*
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.filters.SdkSuppress
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.teller.datastate.LocalDataState
import com.levibostian.teller.datastate.OnlineDataState
import org.junit.After
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ReposRepositoryTest : AndroidIntegrationTestClass {

    @Inject lateinit var db: Database
    @Inject lateinit var responseProcessor: ResponseProcessor
    @Inject lateinit var service: GitHubService

    lateinit var reposDao: ReposDao
    lateinit var reposRepository: ReposRepository

    @Mock private lateinit var sharedPreferences: SharedPreferences
    @Mock private lateinit var preference: Preference<String>
    @Mock private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    private val repo1 = RepoModel(1, "name1", RepoOwnerModel("login1"))
    private val repo2 = RepoModel(2, "name2", RepoOwnerModel("login2"))
    private val repo3 = RepoModel(3, "name3", RepoOwnerModel("login3"))

    private lateinit var application: TestMainApplication

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        application = instrumentation.targetContext.applicationContext as TestMainApplication
        (application.component as MockApplicationComponent).inject(this)

        reposDao = db.reposDao()

        `when`(sharedPreferences.edit()).thenReturn(sharedPrefsEditor)

        reposRepository = ReposRepository(responseProcessor, service, db)
    }

    @After
    fun closeDb() {
        db.close()
    }

    override fun getInstrumentation(): Instrumentation = InstrumentationRegistry.getInstrumentation()

    @Test
    fun nameMe() {
        Truth.assertThat("delete me when want to write actual test").isEqualTo("delete me when want to write actual test")
    }

}