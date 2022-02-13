package com.app.testutils

import com.app.service.util.ThreadUtil
import okhttp3.mockwebserver.*
import okhttp3.mockwebserver.MockWebServer

/**
 * Wrapper around OkHttp [MockWebServer] to give an api that is consistent no matter the networking library used.
 *
 * It's our current practice to use this only in Android instrumentation tests. More specifically, UI tests that test all of the app's code from networking layer to UI layer. Why? Here are recommendations:
 * 1. Using this mock web server and little to no Mockito mocks makes the UI test functions easier to write because you don't have to handle mocking potentially *a lot* of functions but also makes tests better quality.
 * 2. I have tested the UI of Android apps before with by mocking ViewModel dependencies in the UI classes and I got false positives in my tests. My mocks did not behave like my app code did at runtime with threads so with my test suite all passing, my app still crashed almost immediately at runtime. Using something like this makes the app threading perform as the app will.
 * 3. The web server runs on a separate thread sending back responses to your app. We can leverage the Espresso idling resources to wait for OkHttp networking to idle before we go to UI tests code.
 *
 * Note: I have not yet been able to get the web server to work well for integration tests. This is because of the web server running on a separate thread and our test functions expecting to run in a synchronous way using 1 thread for all code execution. The test function always finishes before the web server has time to return a response to the caller. Most of the time, it's still better to use mocking of classes for unit/integration tests anyway with no need to use this. However, if there is a scenario where it makes sense, there may be a way to make the test function wait for networking to finish with a OkHttp custom Dispatcher like the [OkHttpIdlingResource] does.
 */
class MockWebServer(private val mockWebServer: MockWebServer) {

    companion object {
        /**
         * The URL pointing to the web server. The URL is populated by calling [MockWebServer.url] to get the URL and then setting this variable. [MockWebServer.url] requires that you call it on a background thread! Luckily, test functions execute on background threads. So, call [MockWebServer.url] in your test function before your test code starts. Then, use dependency injection singletons to make sure that the app code has the same URL so it gets used there. [MockWebServer.url] starts the server for you. No need to call [MockWebServer.start]
         *
         * There is a way to make this url not a lateinit by using instead:
         * `val url: String = "http://localhost:13714/" // 13714 is a random port number. One that a program is probably not using on the device already`
         * Then, start the mock web server with [MockWebServer.start] providing the port number to the function.
         * However, it's best to use a lateinit because letting OkHttp mockwebserver to always find a port that is unused by the system to avoid tests failing.
         */
        lateinit var url: String
    }

    /**
     * Call from test function as test functions are called on background thread. Use dependency injection singleton to make sure the app and the test is using the [url] as the API endpoint.
     */
    fun start() {
        url = mockWebServer.url("/").toString()
    }

    fun stop() {
        mockWebServer.shutdown()
    }

    fun queue(statusCode: Int, body: String, headers: Map<String, String>? = null) {
        this.queueResponse(statusCode, body, headers)
    }

    fun queueNetworkIssue() {
        // throws an IOException which is a network error
        // https://github.com/square/okhttp/blob/master/mockwebserver/src/test/java/okhttp3/mockwebserver/MockWebServerTest.java#L236
        // https://github.com/square/okhttp/issues/3533
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))
    }

    private fun queueResponse(statusCode: Int, body: String, headers: Map<String, String>? = null) {
        val mockResponse = MockResponse().setResponseCode(statusCode).setBody(body)

        headers?.let { headers ->
            headers.forEach { (key, value) ->
                mockResponse.setHeader(key, value)
            }
        }

        mockWebServer.enqueue(mockResponse)
    }
}
