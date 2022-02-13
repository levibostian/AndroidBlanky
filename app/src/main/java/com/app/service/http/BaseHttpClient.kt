package com.app.service.http

import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*

/**
 * @param hostname Example: https://foo.com/
 */
abstract class BaseHttpClient(private val hostname: String, private val client: OkHttpClient) {

    suspend fun get(path: String): Result<String> {
        val request = Request.Builder()
            .url("$hostname$path")
            .build()

        val response = client.newCall(request).await()

        if (!response.isSuccessful) return Result.failure(IOException("Unexpected code ${response.code}"))
        return Result.success(response.body!!.string())
    }
}

// Help: https://stackoverflow.com/a/623827
internal suspend inline fun Call.await(): Response {
    return suspendCancellableCoroutine { continuation ->
        val callback = ContinuationCallback(this, continuation)
        enqueue(callback)
        continuation.invokeOnCancellation(callback)
    }
}

/** @see Call.await */
internal class ContinuationCallback(
    private val call: Call,
    private val continuation: CancellableContinuation<Response>
) : Callback, CompletionHandler {

    override fun onResponse(call: Call, response: Response) {
        continuation.resume(response)
    }

    override fun onFailure(call: Call, e: IOException) {
        if (!call.isCanceled()) {
            continuation.resumeWithException(e)
        }
    }

    override fun invoke(cause: Throwable?) {
        try {
            call.cancel()
        } catch (_: Throwable) {}
    }
}
