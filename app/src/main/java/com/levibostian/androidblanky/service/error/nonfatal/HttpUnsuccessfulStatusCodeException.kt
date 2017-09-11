package com.levibostian.androidblanky.service.error.nonfatal

class HttpUnsuccessfulStatusCodeException(val statusCode: Int): NonFatalAppError("Sorry, there seems to be an issue. We have been notified. Try again later.")