package com.levibostian.service.error.network

class NotFoundResponseError(override val message: String): Throwable(message)