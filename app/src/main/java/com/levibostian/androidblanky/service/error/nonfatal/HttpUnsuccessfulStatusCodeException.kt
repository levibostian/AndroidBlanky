package com.levibostian.androidblanky.service.error.nonfatal

import com.levibostian.androidblanky.service.model.AppConstants

class HttpUnsuccessfulStatusCodeException(val statusCode: Int): NonFatalAppError(AppConstants.httpUnsuccessfulStatusCodeExceptionMessage)