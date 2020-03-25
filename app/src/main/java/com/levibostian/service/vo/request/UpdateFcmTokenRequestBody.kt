package com.levibostian.service.vo.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateFcmTokenRequestBody(val token: String)