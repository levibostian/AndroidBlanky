package com.levibostian.service.vo.response

import com.levibostian.service.vo.MessageResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordExchangeForAccessTokenResponse(override val message: String,
                                             val user: LoggedInUserVo): MessageResponse