package com.app.mock

import com.app.service.json.JsonAdapter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import javax.inject.Inject

/**
 * Wrapper around OkHttp [MockWebServer] to give an api that is consistent no matter the networking library used.
 */
class MockWebServer @Inject constructor(private val mockWebServer: MockWebServer, private val jsonAdapter: JsonAdapter) {

    val url: String
        get() = mockWebServer.url("/").toString()

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
        val body = jsonAdapter.toJson(data)

        val mockResponse = MockResponse().setResponseCode(statusCode).setBody(body)

        headers?.let { headers ->
            headers.forEach { (key, value) ->
                mockResponse.setHeader(key, value)
            }
        }

        mockWebServer.enqueue(mockResponse)
    }
}
