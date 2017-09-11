package com.levibostian.androidblanky.service.error.nonfatal

class RecoverableBadNetworkConnectionException(val originalError: Throwable) : NonFatalAppError("You have a bad Internet connection. Connect, then try again.")