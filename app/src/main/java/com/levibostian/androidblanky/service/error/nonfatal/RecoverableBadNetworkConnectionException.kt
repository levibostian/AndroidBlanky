package com.levibostian.androidblanky.service.error.nonfatal

import com.levibostian.androidblanky.service.model.AppConstants

class RecoverableBadNetworkConnectionException(val originalError: Throwable) : NonFatalAppError(AppConstants.httpBadNetworkConnectionExceptionMessage)