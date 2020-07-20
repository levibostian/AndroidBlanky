package com.app.service.vo.response.error

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnauthorizedError(val message: String)
