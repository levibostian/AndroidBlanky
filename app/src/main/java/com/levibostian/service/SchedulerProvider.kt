package com.levibostian.service

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SchedulerProvider {

    val background: Scheduler
        get() = Schedulers.io()

    val main: Scheduler
        get() = AndroidSchedulers.mainThread()
}
