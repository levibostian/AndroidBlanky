package com.levibostian.androidblanky.rx

import org.json.JSONObject
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result.response
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

abstract class HttpDisposableObserver<T>(handler: HttpErrorMessageHandler) : DisposableObserver<T>() {

    private val weakReference: WeakReference<HttpErrorMessageHandler> = WeakReference(handler)

    override fun onComplete() {
        // don't need to do anything. onNext() will receive the response.
    }

    override fun onError(e: Throwable) {
        val handler = weakReference.get()

        handler?.handleHttpErrorMessage(getErrorMessage(e) ?: "Unknown error")
    }

    fun getErrorMessage(error: Throwable): String? {
        if (error is HttpException) {
            val responseBody = error.response().errorBody()

            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<HttpErrorResponseBody>(HttpErrorResponseBody::class.java)
            return jsonAdapter.fromJson(responseBody?.string())?.getErrorMessage()
        } else if (error is SocketTimeoutException) {
            return "There was an issue with your Internet connection. Please, try again."
        } else {
            throw error
        }
    }

}
