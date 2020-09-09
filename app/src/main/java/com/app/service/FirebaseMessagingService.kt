package com.app.service

import android.app.Service
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * This class is an Android [Service]. [Service]s can use Hilt to directly inject dependencies into them. However, I have found that when running UI/integration tests in Firebase Test Lab with FCM that this service tries to get created well before any of my test code can execute. This results in the Hilt dependency graph not having any of the required mocks it needs to successfully run in our tests. This results in our test application crashing (see the stacktrace pasted below). This crash only happens 50% of the time but that's far too much! The entire test suite fails and does not run because of this crash.
 *
 * Therefore, this class contains a hack. As long as this class does *not* use Hilt dependency injection, the hack will work. We delegate the work to another class that uses Hilt dependency injection. The hack is that we do not use Hilt dependency injection in this class, ever. We also manually initialize FCM in our app. This means that none of the functions in this [Service] class will ever execute because FCM is not initialized.
 *
Caused by: java.lang.IllegalStateException: The component was not created. Check that you have added the HiltAndroidRule.
at dagger.hilt.internal.Preconditions.checkState(Preconditions.java:83)
at dagger.hilt.android.internal.testing.TestApplicationComponentManager.generatedComponent(TestApplicationComponentManager.java:79)
at dagger.hilt.android.testing.HiltTestApplication.generatedComponent(HiltTestApplication.java:49)
at dagger.hilt.EntryPoints.get(EntryPoints.java:46)
at dagger.hilt.android.internal.managers.ServiceComponentManager.createComponent(ServiceComponentManager.java:70)
at dagger.hilt.android.internal.managers.ServiceComponentManager.generatedComponent(ServiceComponentManager.java:58)
at com.app.service.Hilt_FirebaseMessagingService.generatedComponent(Hilt_FirebaseMessagingService.java:53)
at com.app.service.Hilt_FirebaseMessagingService.inject(Hilt_FirebaseMessagingService.java:48)
at com.app.service.Hilt_FirebaseMessagingService.onCreate(Hilt_FirebaseMessagingService.java:27)
at android.app.ActivityThread.handleCreateService(ActivityThread.java:3953)
 */
class FirebaseMessagingService : FirebaseMessagingService() {

    private val pushNotificationProcessor by lazy {
        PushNotificationProcessor(application)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // No need to block this code from running because it never should. This code will only run if the MainApplication code runs. It will not run for tests because tests use a separate test Application class. We setup FCM for manual initialization which means we will not receive any messages when tests run.
        pushNotificationProcessor.processFirebaseMessage(message)
    }

    override fun onNewToken(token: String) {
//        logger.breadcrumb(
// //            this, "Push notification token received",
// //            bundleOf(
// //                Pair("token", token)
// //            )
// //        )
    }
}
