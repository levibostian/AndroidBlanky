package com.levibostian.androidblanky.service.error.network.type

import com.levibostian.androidblanky.service.error.network.type.ConflictResponseErrorType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ConflictResponseErrorTypeAdapter {

    @FromJson fun eventFromJson(eventJson: String?): ConflictResponseErrorType {
        if (eventJson == null) throw RuntimeException("Cannot be null")
        return ConflictResponseErrorType.fromString(eventJson)
    }

    @ToJson fun eventToJson(event: ConflictResponseErrorType): String? {
        return event.type
    }

}