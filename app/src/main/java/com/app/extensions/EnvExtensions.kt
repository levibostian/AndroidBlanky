package com.app.extensions

import com.app.BuildConfig
import com.app.Env

val Env.isDevelopment: Boolean
    get() = Env.development == "true"

val Env.appVersion: String
    get() = BuildConfig.VERSION_NAME
