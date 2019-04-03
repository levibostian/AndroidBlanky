package com.levibostian.androidblanky.service.repository

import com.levibostian.androidblanky.service.AppService
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.vo.request.PasswordlessEmailLoginBody
import com.levibostian.androidblanky.service.vo.request.UpdateFcmTokenRequestBody
import com.levibostian.androidblanky.service.vo.response.MessageResponseVo
import com.levibostian.teller.repository.OnlineRepository
import io.reactivex.Single


class UserRepository(private val appService: AppService,
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