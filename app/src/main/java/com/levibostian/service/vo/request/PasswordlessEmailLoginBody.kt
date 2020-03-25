package com.levibostian.service.vo.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordlessEmailLoginBody(val email: String)