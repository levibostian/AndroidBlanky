package com.levibostian.androidblanky.service.error.network

import com.levibostian.androidblanky.service.error.network.type.ConflictResponseErrorType

/**
 * API can return an [error_id] String which can describe the error so the UI can handle it.
 */
class ConflictResponseError(override val message: String,
                            val error_id: ConflictResponseErrorType): Throwable(message)