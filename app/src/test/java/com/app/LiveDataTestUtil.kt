package com.app

// From https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/test-common/java/com/android/example/github/util/LiveDataTestUtil.kt
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 *
 * I added [predicate] from the original implementation because some tests were failing from not running synchronously. I believe this is because of mixing Rx and LiveData. The Rx runs on different threads?
 *
 * From: https://github.com/android/architecture-components-samples/blob/85587900b68a5d3a7edf95065fe2d8c768e66164/GithubBrowserSample/app/src/test-common/java/com/android/example/github/util/LiveDataTestUtil.kt
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {},
    predicate: ((T) -> Boolean)? = null
): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    val valueFound: (T) -> Unit = { value ->
        data = value
        latch.countDown()
    }

    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            if (predicate != null) {
                if (predicate(o as T)) {
                    valueFound(o)
                    this@getOrAwaitValue.removeObserver(this)
                }
            } else {
                valueFound(o as T)
                this@getOrAwaitValue.removeObserver(this)
            }
        }
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
