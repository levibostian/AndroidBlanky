package com.levibostian.androidblanky.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.Observable

fun <T> LiveData<T>.toObservable(lifecycleOwner: LifecycleOwner): Observable<T> {
    return Observable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))
}