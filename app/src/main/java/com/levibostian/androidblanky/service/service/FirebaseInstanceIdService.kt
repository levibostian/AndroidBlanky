package com.levibostian.androidblanky.service.service

import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import javax.inject.Inject

class FirebaseInstanceIdService: FirebaseInstanceIdService() {

    @Inject lateinit var userManager: UserManager

    override fun onTokenRefresh() {
        (application as MainApplication).component.inject(this)

        userManager.fcmPushNotificationToken = FirebaseInstanceId.getInstance().token
    }

}