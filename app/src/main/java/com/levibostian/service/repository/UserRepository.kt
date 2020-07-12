package com.levibostian.service.repository

import com.levibostian.service.api.AppService
import com.levibostian.service.util.ResponseProcessor
import com.levibostian.service.vo.request.PasswordlessEmailLoginBody
import com.levibostian.service.vo.request.UpdateFcmTokenRequestBody
import com.levibostian.service.vo.response.MessageResponseVo
import com.levibostian.teller.repository.OnlineRepository
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(private val appService: AppService,
                                         private val responseProcessor: ResponseProcessor) {

    fun updateFcmToken(fcmToken: String): Single<OnlineRepository.FetchResponse<MessageResponseVo>> {
        return appService.updateFcmToken(UpdateFcmTokenRequestBody(fcmToken))
                .map {
                    responseProcessor.process(it)
                }
    }

    fun loginPasswordlessEmail(email: String): Single<OnlineRepository.FetchResponse<MessageResponseVo>> {
        return appService.login(PasswordlessEmailLoginBody(email))
                .map {
                    responseProcessor.process(it)
                }
    }

}