package com.levibostian.androidblanky.service.util

import android.net.Uri
import android.os.Bundle
import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.analytics.FirebaseAnalytics

class InstallReferrerProcessor {

    companion object {
        fun process(referrerDetails: ReferrerDetails): Bundle {
            val referrerSource: Uri = Uri.parse(referrerDetails.installReferrer)
            // Currently not being used.
            // response.referrerClickTimestampSeconds
            // response.installBeginTimestampSeconds

            val bundle = Bundle()
            referrerSource.getQueryParameter("utm_source")?.let { bundle.putString(FirebaseAnalytics.Param.SOURCE, it) }
            referrerSource.getQueryParameter("utm_medium")?.let { bundle.putString(FirebaseAnalytics.Param.MEDIUM, it) }
            referrerSource.getQueryParameter("utm_campaign")?.let { bundle.putString(FirebaseAnalytics.Param.CAMPAIGN, it) }
            referrerSource.getQueryParameter("utm_term")?.let { bundle.putString(FirebaseAnalytics.Param.TERM, it) }
            referrerSource.getQueryParameter("utm_content")?.let { bundle.putString(FirebaseAnalytics.Param.CONTENT, it) }

            return bundle
        }
    }

}