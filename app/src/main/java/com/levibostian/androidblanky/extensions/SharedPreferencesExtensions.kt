package com.levibostian.androidblanky.extensions

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import java.util.*

fun SharedPreferences.observeString(key: String, defaultValue: String = ""): Observable<String> {
    return rxInstance().getString(key, defaultValue).asObservable()
}

fun SharedPreferences.rxInstance(): RxSharedPreferences {
    return RxSharedPreferences.create(this)
}

fun SharedPreferences.getDate(key: String, default: Date?): Date? {
    val time: Long = getLong(key, 0)
    return if (time == 0L) default
    else Date(time)
}

fun SharedPreferences.Editor.putDate(key: String, value: Date): SharedPreferences.Editor {
    putLong(key, value.time)
    return this
}