package com.levibostian.androidblanky.service.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAppAnalytics(context: Context): AppAnalytics {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun setUserId(id: String?) {
        firebaseAnalytics.setUserId(id)
    }

}