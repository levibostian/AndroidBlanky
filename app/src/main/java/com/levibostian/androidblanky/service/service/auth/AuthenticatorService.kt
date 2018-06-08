package com.levibostian.androidblanky.service.service.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.levibostian.androidblanky.service.auth.AccountAuthenticator

class AuthenticatorService: Service() {

    override fun onBind(intent: Intent): IBinder {
        return AccountAuthenticator(this).iBinder
    }

}
