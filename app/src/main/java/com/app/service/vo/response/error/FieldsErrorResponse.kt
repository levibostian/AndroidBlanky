package com.app.service.vo.response.error

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FieldsErrorResponse(val message: String)

// We need an exception to log to the logger, but we can't use JSON deserialization on Throwable child.
class FieldsErrorException(message: String) : Throwable(message)
