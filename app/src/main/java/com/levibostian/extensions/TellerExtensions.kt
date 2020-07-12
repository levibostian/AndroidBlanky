package com.levibostian.extensions

import com.levibostian.teller.repository.OnlineRepository
import com.levibostian.teller.repository.OnlineRepositoryFetchResponse

fun <T: OnlineRepositoryFetchResponse> Result<T>.toFetchResponse(): OnlineRepository.FetchResponse<T> {
    return if (isFailure) OnlineRepository.FetchResponse.fail<T>(exceptionOrNull()!!)
    else OnlineRepository.FetchResponse.success(getOrNull()!!)
}

fun <T, R: OnlineRepositoryFetchResponse> Result<T>.mapSuccess(map: (value: T) -> R): OnlineRepository.FetchResponse<R> {
    return if (isFailure) OnlineRepository.FetchResponse.fail<R>(exceptionOrNull()!!)
    else OnlineRepository.FetchResponse.success(map(getOrNull()!!))
}