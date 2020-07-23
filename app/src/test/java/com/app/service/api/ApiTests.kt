package com.app.service.api

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.app.extensions.result
import com.app.service.error.network.HttpRequestError
import com.app.service.error.network.NoInternetConnectionException
import com.app.service.logger.Logger
import com.app.service.vo.response.MessageResponseVo
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.io.IOException
import kotlin.test.fail

@RunWith(AndroidJUnit4::class)
class ApiTests : UnitTest() {

    @Mock lateinit var logger: Logger
    @Mock lateinit var service: GitHubService

    private lateinit var api: Api

    @Before
    override fun setup() {
        super.setup()

        api = ApiForTesting(context, logger)
    }

    @Test
    fun `request - given no internet connection, expect give me network error`() {
        val actual = api.request(Single.just(Result.error<String>(NoInternetConnectionException("")))).blockingGet().exceptionOrNull()!! as HttpRequestError

        assertThat(actual.faultType).isEqualTo(HttpRequestError.FaultType.NETWORK)
        assertThat(actual.message).isNotEmpty()
    }

    @Test
    fun `request - given bad internet connection, expect give me network error`() {
        val actual = api.request(Single.just(Result.error<String>(IOException("")))).blockingGet().exceptionOrNull()!! as HttpRequestError

        assertThat(actual.faultType).isEqualTo(HttpRequestError.FaultType.NETWORK)
        assertThat(actual.message).isNotEmpty()
    }

    @Test
    fun `request - given failure not handled, expect log error, expect do not show error message to user`() {
        val given = IllegalArgumentException("do-not-show-this")
        val actual = api.request(Single.just(Result.error<String>(given))).blockingGet().exceptionOrNull()!! as HttpRequestError

        assertThat(actual.faultType).isEqualTo(HttpRequestError.FaultType.DEVELOPER)
        assertThat(actual.message).isNotEqualTo(given.message)

        val errorCaptor = argumentCaptor<Throwable>()
        verify(logger).errorOccurred(errorCaptor.capture())
        assertThat(errorCaptor.firstValue.message).isEqualTo(given.message)
    }

    @Test
    fun `request - given successful response expect successful result, expect get parsed body`() {
        val givenResponse = MessageResponseVo("")

        val actual = api.request(Single.just(Response.success(givenResponse).result)).blockingGet().getOrNull()!!

        assertThat(actual).isEqualTo(givenResponse)
    }

    @Test
    fun `request - given unsuccessful response, expect call extra error handling`() {
        val givenResponse = MessageResponseVo("")
        val givenExtraErrorHandling = HttpRequestError.userError("message-to-user", null)

        var isCalled = false
        val actual = api.request(
            Single.just(com.app.extensions.Response.error<MessageResponseVo, MessageResponseVo>(400, givenResponse).result),
            extraErrorHandling = { processedResponse ->
                isCalled = true

                givenExtraErrorHandling
            }
        ).blockingGet().exceptionOrNull()!! as HttpRequestError

        if (!isCalled) fail("extra error handling should have been called")
        assertThat(actual.message).isEqualTo(givenExtraErrorHandling.message)
        assertThat(actual.underlyingError).isNull()
        assertThat(actual.faultType).isEqualTo(HttpRequestError.FaultType.USER)
    }

    @Test
    fun `request - given unsuccessful response, expect call extra success handling`() {
        val givenResponse = MessageResponseVo("")

        var isCalled = false
        val actual = api.request(
            Single.just(com.app.extensions.Response.error<MessageResponseVo, MessageResponseVo>(400, givenResponse).result),
            extraSuccessHandling = { processedResponse ->
                isCalled = true

                givenResponse
            }
        ).blockingGet().getOrNull()!!

        if (!isCalled) fail("extra success handling should have been called")
        assertThat(actual).isEqualTo(givenResponse)
    }

    @Test
    fun `request - given unsuccessful response and no extra error handing, expect log error`() {
        val givenResponse = MessageResponseVo("")

        api.request(Single.just(com.app.extensions.Response.error<MessageResponseVo, MessageResponseVo>(400, givenResponse).result)).blockingGet().exceptionOrNull()!! as HttpRequestError

        verify(logger).errorOccurred(any())
    }

    private class ApiForTesting(
        context: Context,
        logger: Logger
    ) : Api(context, logger) {

        override fun handleUnsuccessfulStatusCode(processedResponse: ProcessedResponse): HttpRequestError? = null
    }
}
