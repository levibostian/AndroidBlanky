package com.levibostian.androidblanky.service.service

class HttpResponseConstants {

    companion object {
        const val SystemError = 500
        const val UserEnteredBadDataError = 400
        const val ConflictError = 409
        const val ForbiddenError = 403
        const val Unauthorized = 401
        const val FieldsError = 422
        const val RateLimitingError = 429
    }

}