package com.levibostian.service.json.type_adapter

import android.net.Uri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class UriJsonAdapter {

    @FromJson
    fun eventFromJson(eventJson: String?): Uri? {
        if (eventJson == null) return null

        return Uri.parse(eventJson)
    }

    @ToJson
    fun eventToJson(event: Uri?): String? {
        return event?.toString()
    }

}