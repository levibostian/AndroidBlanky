package com.app.mock

import com.app.service.json.JsonAdapter
import com.app.service.util.ThreadUtil
import com.nhaarman.mockitokotlin2.mock
import okhttp3.mockwebserver.*
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Inject

/**
 * Wrapper around OkHttp [MockWebServer] to give an api that is consistent no matter the networking library used.
 */
class MockWebServer @Inject constructor(private val mockWebServer: MockWebServer) {

    lateinit var url: String

    /**
     * Call from test function as test functions are called on background thread. Use dependency injection singleton to make sure the app and the test is using the same instance.
     */
    fun start() {
        assert(ThreadUtil.isBackgroundThread) // the mock web server must be on the background thread to start. When you try to get the URL, it starts if it has not started already.

        this.url = mockWebServer.url("/").toString()
    }

    fun stop() {
        mockWebServer.shutdown()
    }

    fun <T : Any> queue(statusCode: Int, data: T, headers: Map<String, String>? = null) {
        this.queueResponse(statusCode, data, headers)
    }

    fun <T : Any> queue(statusCode: Int, data: Array<T>, headers: Map<String, String>? = null) {
        this.queueResponse(statusCode, data, headers)
    }

    fun queueNetworkIssue() {
        // throws an IOException which is a network error
        // https://github.com/square/okhttp/blob/master/mockwebserver/src/test/java/okhttp3/mockwebserver/MockWebServerTest.java#L236
        // https://github.com/square/okhttp/issues/3533
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
    }

    private fun <T : Any> queueResponse(statusCode: Int, data: T, headers: Map<String, String>? = null) {
        val body = JsonAdapter.toJson(data)

        val mockResponse = MockResponse().setResponseCode(statusCode).setBody(body)

        headers?.let { headers ->
            headers.forEach { (key, value) ->
                mockResponse.setHeader(key, value)
            }
        }

        mockWebServer.enqueue(mockResponse)
    }
}
