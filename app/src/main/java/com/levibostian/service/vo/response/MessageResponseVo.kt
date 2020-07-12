package com.levibostian.service.vo.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageResponseVo(val message: String)