package com.levibostian.service.vo.response

import com.levibostian.service.vo.MessageResponse
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageResponseVo(override val message: String): MessageResponse