package com.levibostian.androidblanky.service.manager

import android.app.Activity

/**
 * Used to store account for a user onto the device in a safe way.
 *
 * In our case, using Android's account manager.
 */
interface DeviceAccountManager {
    val accessToken: String?

    fun continueLoginFlow(activity: Activity, forceLogout: Boolean, passwordlessToken: String?, done: (success: Boolean) -> Unit)

    fun login(email: String, accessToken: String)
    fun logout()
}