package com.levibostian.extensions

import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

val <T> Response<T>.result: Result<T>
    get() = Result.response(this)
