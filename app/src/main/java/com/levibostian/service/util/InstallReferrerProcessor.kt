package com.levibostian.service.util

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.service.logger.Logger

/**
 * Google Play Install Referrer library for UTM tracking of app install.
 * Details: https://developer.android.com/google/play/installreferrer/library.html
 */
class InstallReferrerProcessor(
    private val sharedPreferences: SharedPreferences,
    private val logger: Logger
) {

    fun process(context: Context) {
        processHelper(context, 0)
    }

    // We are not logging this info at this time. It may not be used in the app, so it's disabled for now until the implementation is rethought.
    private fun processHelper(context: Context, numberAttempts: Int) {
//        val referrerClient = InstallReferrerClient.newBuilder(context).build()
//        referrerClient.startConnection(object : InstallReferrerStateListener {
//            @SuppressLint("ApplySharedPref")
//            override fun onInstallReferrerSetupFinished(responseCode: Int) {
//                when (responseCode) {
//                    InstallReferrerClient.InstallReferrerResponse.OK -> {
//                        if (sharedPreferences.getBoolean(SharedPrefersKeys.FIRST_APP_LAUNCH, true)) {
//                            val response: ReferrerDetails = referrerClient.installReferrer
//
//                            // logger.performedEvent(Logger.ActivityEvent.InstalledAppReferral(), processReferrerData(response))
//
//                            referrerClient.endConnection()
//                            sharedPreferences.edit {
//                                putBoolean(SharedPrefersKeys.FIRST_APP_LAUNCH, false)
//                            }
//                        }
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
//                        // API not available on the current Play Store app
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
//                        // Connection could not be established
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
//                        logger.errorOccurred(RuntimeException("There is a developer error with Play Install library"))
//                    }
//                    InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED -> {
//                        if (numberAttempts > 3) logger.errorOccurred(RuntimeException("Service disconnected with Play Install library"))
//                        else processHelper(context, numberAttempts + 1)
//                    }
//                }
//            }
//            override fun onInstallReferrerServiceDisconnected() {
//            }
//        })
    }

    private fun processReferrerData(referrerDetails: ReferrerDetails): Bundle {
        val referrerSource: Uri = Uri.parse(referrerDetails.installReferrer)
        // Currently not being used.
        // response.referrerClickTimestampSeconds
        // response.installBeginTimestampSeconds

        return Bundle().apply {
            referrerSource.getQueryParameter("utm_source")?.let { putString(FirebaseAnalytics.Param.SOURCE, it) }
            referrerSource.getQueryParameter("utm_medium")?.let { putString(FirebaseAnalytics.Param.MEDIUM, it) }
            referrerSource.getQueryParameter("utm_campaign")?.let { putString(FirebaseAnalytics.Param.CAMPAIGN, it) }
            referrerSource.getQueryParameter("utm_term")?.let { putString(FirebaseAnalytics.Param.TERM, it) }
            referrerSource.getQueryParameter("utm_content")?.let { putString(FirebaseAnalytics.Param.CONTENT, it) }
        }
    }
}
