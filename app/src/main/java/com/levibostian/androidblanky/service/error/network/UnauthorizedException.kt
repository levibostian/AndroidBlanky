package com.levibostian.androidblanky.service.error.network

// When a HTTP response comes in at 401
class UnauthorizedException(message: String): Throwable(message)