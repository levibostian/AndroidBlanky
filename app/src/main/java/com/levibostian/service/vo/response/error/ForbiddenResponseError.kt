package com.levibostian.service.vo.response.error

import com.levibostian.service.json.JsonAdapter
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForbiddenResponseError(val error_message: String) {

    companion object {
        fun from(body: String): ForbiddenResponseError = JsonAdapter.fromJson(body)
    }
}
