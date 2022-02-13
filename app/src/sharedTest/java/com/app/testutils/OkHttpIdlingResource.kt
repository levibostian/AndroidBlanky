package com.app.testutils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import okhttp3.Dispatcher
import okhttp3.OkHttpClient

class OkHttpIdlingResource private constructor(private val name: String, private val dispatcher: Dispatcher) : IdlingResource {

    companion object {
        fun create(name: String, client: OkHttpClient): OkHttpIdlingResource = OkHttpIdlingResource(name, client.dispatcher)
    }

    init {
        dispatcher.idleCallback = Runnable {
            val callback = callback
            callback?.onTransitionToIdle()
        }
    }

    @Volatile var callback: ResourceCallback? = null

    override fun getName(): String = name

    override fun isIdleNow(): Boolean = dispatcher.runningCallsCount() == 0

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {
        this.callback = callback
    }
}
