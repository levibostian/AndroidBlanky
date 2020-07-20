package com.app.service.vo.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoggedInUserVo(
    val id: Int,
    val email: String,
    val username: String?,
    val access_token: String
)
