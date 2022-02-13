package com.app

import androidx.test.espresso.IdlingRegistry
import com.app.service.DispatcherProvider
import com.app.service.store.KeyValueStorage
import com.app.testutils.MockWebServer
import com.app.testutils.OkHttpIdlingResource
import com.nhaarman.mockitokotlin2.whenever
import javax.inject.Inject
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

/**
 * Base class for running tests on JVM or Android device. This is for non-UI related tests.
 */
abstract class BaseTest : Test() {

    protected lateinit var mockWebServer: MockWebServer
    @Inject lateinit var okHttpClient: OkHttpClient
    private lateinit var okHttpIdlingResource: OkHttpIdlingResource

    private var testFunctionRunningCoroutine = false
    private var testFunctionDoneRunningCoroutine = false

    /**
     * Test code that launches a new coroutine. The problem with running code that launches a coroutine inside of it is that it could run asynchronously if not setup correctly. This results in possible false positives in test functions if the end of a test function gets hit but an asynchronous coroutine is still running. Use this to prevent false positives.
     *
     * ```
     * testCoroutine { done ->
     *   viewModel.getPokemon("name") { actual -> // <-- this function call will launch a new coroutine inside of it.
     *     assertThat(actual.isSuccess).isTrue()
     *
     *     done()
     *   }
     * }
     * ```
     */
    protected fun testCoroutine(run: (done: () -> Unit) -> Unit) {
        testFunctionRunningCoroutine = true
        testFunctionDoneRunningCoroutine = false

        run {
            testFunctionDoneRunningCoroutine = true
        }
    }

    // Below are some mocked (mocked in the Hilt modules) classes that you can use in integration tests.
    @Inject lateinit var dispatcherProvider: DispatcherProvider
    @Inject lateinit var keyValueStorage: KeyValueStorage

    @Before
    override fun setup() {
        super.setup()

        /**
         * Make sure to start the mock web server before dependency injection as we need a URL to the web server to be injected into the app.
         */
        mockWebServer = MockWebServer(okhttp3.mockwebserver.MockWebServer())
        mockWebServer.start()

        // if this doesn't work, you can use the mockito test rule: MockitoJUnit.rule().
        MockitoAnnotations.initMocks(provideTestClass())

        diRule.inject()

        keyValueStorage.deleteAll()

        okHttpIdlingResource = OkHttpIdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(okHttpIdlingResource)

        whenever(dispatcherProvider.io()).thenReturn(mainCoroutineRule.testDispatcher)
        whenever(dispatcherProvider.main()).thenReturn(mainCoroutineRule.testDispatcher)
    }

    @After
    override fun teardown() {
        super.teardown()

        if (testFunctionRunningCoroutine) {
            if (!testFunctionDoneRunningCoroutine) throw RuntimeException("Coroutine under test is still running and has not yet been finished.")
        }

        IdlingRegistry.getInstance().unregister(okHttpIdlingResource)
        mockWebServer.stop()
    }
}
