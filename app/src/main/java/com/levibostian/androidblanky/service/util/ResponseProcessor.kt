package com.levibostian.androidblanky.service.util

import android.content.Context
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.error.network.*
import com.levibostian.androidblanky.service.json.JsonAdapter
import com.levibostian.androidblanky.service.logger.Logger
import com.levibostian.androidblanky.service.service.HttpResponseConstants
import com.levibostian.androidblanky.service.vo.MessageResponse
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.repository.OnlineRepositoryFetchResponse
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.io.IOException
import javax.inject.Inject

@OpenForTesting
class ResponseProcessor @Inject constructor(private val context: Context,
                                            private val logger: Logger,
                                            private val jsonAdapter: JsonAdapter) {

    private val humanReadableUnhandledResultError by lazy { UnhandledHttpResultException(context.getString(R.string.fatal_network_error_message)) }

    fun <RESPONSE: OnlineRepositoryFetchResponse> process(result: Result<RESPONSE>, extraProcessing: ((code: Int, response: Response<RESPONSE>, jsonAdapter: JsonAdapter) -> Throwable?)? = null): OnlineRepository.FetchResponse<RESPONSE> {
        return processRequestFailure(result) ?:
        processRequestSuccessful(result.response()!!, extraProcessing) ?:
        processUnhandledResult(result)
    }

    private fun <RESPONSE: OnlineRepositoryFetchResponse> processRequestFailure(result: Result<RESPONSE>): OnlineRepository.FetchResponse<RESPONSE>? {
        when (result.error()) {
            is NoInternetConnectionException,
            is UnauthorizedException -> result.error()
            is IOException -> NetworkConnectionIssueException(context.getString(R.string.error_network_connection_issue))
            null -> { null }
            else -> {
                // According to Retrofit's result.error() Javadoc, if the error is not an instance of IOException, it's a programming error and should be looked at. Throw it so we can see it and fix it.
                logger.errorOccurred(result.error()!!)
                humanReadableUnhandledResultError
            }
        }?.let { requestError ->
            return OnlineRepository.FetchResponse.fail(requestError)
        }

        return null
    }

    private fun <RESPONSE: OnlineRepositoryFetchResponse> processRequestSuccessful(response: Response<RESPONSE>, extraProcessing: ((code: Int, response: Response<RESPONSE>, jsonAdapter: JsonAdapter) -> Throwable?)? = null): OnlineRepository.FetchResponse<RESPONSE>? {
        if (response.isSuccessful) { // Successful (status code < 400)
            return OnlineRepository.FetchResponse.success(response.body()!!)
        }

        val errorBody = response.errorBody()?.string()
        val statusCode = response.code()
        when (statusCode) {
            in HttpResponseConstants.SystemError..600 -> ServerErrorException(context.getString(R.string.error_500_600_response_code))
            HttpResponseConstants.UserEnteredBadDataError -> {
                jsonAdapter.fromJson(errorBody!!, UserEnteredBadDataResponseError::class.java)
            }
            HttpResponseConstants.RateLimitingError -> {
                RateLimitingResponseError(context.getString(R.string.error_rate_limiting_response))
            }
            HttpResponseConstants.ConflictError -> {
                jsonAdapter.fromJson(errorBody!!, ConflictResponseError::class.java)
            }
            HttpResponseConstants.ForbiddenError -> {
                jsonAdapter.fromJson(errorBody!!, ForbiddenResponseError::class.java)
            }
            else -> extraProcessing?.invoke(statusCode, response, jsonAdapter)
            /**
             * Do not list FieldsError here. If a 422 happens, it might be a problem with the app and not the user. Therefore, if a 422 could happen, have the API call handle it individually instead of handle it globally here.
             */
        }?.let { responseError ->
            return OnlineRepository.FetchResponse.fail(responseError)
        }

        return null
    }

    private fun <RESPONSE: OnlineRepositoryFetchResponse> processUnhandledResult(result: Result<RESPONSE>): OnlineRepository.FetchResponse<RESPONSE> {
        val unhandledErrorForLogging = UnhandledHttpResultException("Fatal HTTP network call. ${result.response()?.toString() ?: "(no HTTP response found)."}")
        if (BuildConfig.DEBUG) throw unhandledErrorForLogging
        logger.errorOccurred(unhandledErrorForLogging)

        return OnlineRepository.FetchResponse.fail(humanReadableUnhandledResultError)
    }

}