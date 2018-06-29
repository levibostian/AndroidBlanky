package com.levibostian.androidblanky.service.interceptor

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.model.AppConstants
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.util.*

// In the APIs that I build, I have a design pattern that I use to help keep user's apps up-to-date.
//
// Let's say that the client side of your app is using v1.0 of an API. The JSON model you are using matches that v1.0.
// Let's also say that your API updates to v1.1 and the JSON model changes to add a new feature to it: the ability to add friends to your profile.
// Since the user's app is on v1.0 of the JSON model, it cannot deserialize the JSON and use this new feature. The API knows you are on this old version and for a better user experience, suggests that you download this new version since it is a brand new feature.
// So, in the JSON response, I send back "missing_data": true to the JSON body. If we intercept that in the response, show the user a notification to prompt them to update the app.
class MissingDataResponseInterceptor(private val applicationContext: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val response = chain.proceed(request)

        if (response.isSuccessful && response.body() != null) {
            val responseBodyCopy = response.peekBody(java.lang.Long.MAX_VALUE) // We can only call response.body().string() *once* from Okhttp because of it trying to be conservative to memory. So, we must create a copy of the body. https://github.com/square/okhttp/issues/1240#issuecomment-330813274
            val responseBody = responseBodyCopy.string()

            val json = JSONObject(responseBody)
            if (json.has("missing_data")) {
                val isMissingData: Boolean = json["missing_data"] as Boolean
                if (isMissingData) {
                    val openPlayStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.PLAY_STORE_LINK))
                    val updateAppPendingIntent = PendingIntent.getBroadcast(applicationContext, NotificationChannelManager.UPDATE_APP_ID, openPlayStoreIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val notification = NotificationCompat.Builder(applicationContext, NotificationChannelManager.errors.id)
                            .setSmallIcon(R.drawable.ic_error_yellow_24dp)
                            .setContentTitle(String.format(Locale.getDefault(), applicationContext.getString(R.string.update_app_title), applicationContext.getString(R.string.app_name)))
                            .setContentText(String.format(Locale.getDefault(), applicationContext.getString(R.string.update_app_message), applicationContext.getString(R.string.app_name)))
                            .addAction(NotificationCompat.Action(R.drawable.ic_file_download_white_24dp, "Update app", updateAppPendingIntent))
                            .build()

                    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    notificationManager.notify(NotificationChannelManager.UPDATE_APP_ID, notification)
                }
            }
        }

        return response
    }

}