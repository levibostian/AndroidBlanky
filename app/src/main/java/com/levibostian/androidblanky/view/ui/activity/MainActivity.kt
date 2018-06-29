package com.levibostian.androidblanky.view.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.crashlytics.android.answers.FirebaseAnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.util.InstallReferrerProcessor
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import com.levibostian.wendy.service.Wendy
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var sharedPreferences: SharedPreferences
    @Inject lateinit var dataDestroyer: DataDestroyer

    private lateinit var referrerClient: InstallReferrerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).component.inject(this)

        setContentView(R.layout.activity_toolbar_fragment_container)
        setSupportActionBar(activity_toolbar_toolbar as Toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.activity_toolbar_fragment_container, MainFragment.newInstance()).commit()
        }

        // I am capturing the referrer here instead of in LaunchActivity because (1) The user is logged into the app at this point and Firebase Analytics will then be associated with a user id. (2) I should only really care about where users came from that successfully logged into the app as they are users who want to use the product.
        referrerClient = InstallReferrerClient.newBuilder(this).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        if (sharedPreferences.getBoolean(SharedPrefersKeys.FIRST_APP_LAUNCH, false)) {
                            val response: ReferrerDetails = referrerClient.installReferrer

                            FirebaseAnalytics.getInstance(this@MainActivity).logEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS, InstallReferrerProcessor.process(response))

                            referrerClient.endConnection()
                            sharedPreferences.edit().putBoolean(SharedPrefersKeys.FIRST_APP_LAUNCH, false).apply()
                        }
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection could not be established
                    }
                }
            }
            override fun onInstallReferrerServiceDisconnected() {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)

        // Users can remove accounts in the settings app on the device. Check if they did while the app was in the background.
        if (!userManager.isUserLoggedIn()) {
            EventBus.getDefault().post(LogoutUserEvent(true))
        }
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LogoutUserEvent) {
        if (event.lockAccount) dataDestroyer.destroyAccountManagerAccounts()

        startActivity(LaunchActivity.getIntent(this, true))
        finish()
    }

}