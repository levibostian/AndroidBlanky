package com.levibostian.androidblanky.service

import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.vo.request.ExchangePasswordTokenForAccessTokenRequestBody
import com.levibostian.androidblanky.service.vo.request.PasswordlessEmailLoginBody
import com.levibostian.androidblanky.service.vo.request.UpdateFcmTokenRequestBody
import com.levibostian.androidblanky.service.vo.response.MessageResponseVo
import com.levibostian.androidblanky.service.vo.response.PasswordExchangeForAccessTokenResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppService {

    @POST("user/fcm")
    fun updateFcmToken(@Body body: UpdateFcmTokenRequestBody): Single<Result<MessageResponseVo>>

    @POST("user/login")
    fun login(@Body body: PasswordlessEmailLoginBody): Single<Result<MessageResponseVo>>

    @POST("user/login/token")
    fun exchangePasswordTokenForAccessToken(@Body body: ExchangePasswordTokenForAccessTokenRequestBody): Single<Result<PasswordExchangeForAccessTokenResponse>>

}