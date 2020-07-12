package com.levibostian.service.vo.response.error


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserEnteredBadDataError(val error_message: String)