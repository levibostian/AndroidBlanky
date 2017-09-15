package com.levibostian.androidblanky.service.error.nonfatal

import com.levibostian.androidblanky.service.model.AppConstants

class ServerErrorException : NonFatalAppError(AppConstants.http500StatusErrorMessage)