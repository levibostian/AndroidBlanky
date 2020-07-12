package com.levibostian.extensions

import com.levibostian.BuildConfig
import com.levibostian.Env

val Env.isDevelopment: Boolean
    get() = Env.development == "true"

val Env.appVersion: String
    get() = BuildConfig.VERSION_NAME