package com.levibostian.androidblanky.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {

    @Throws(JsonParseException::class, ParseException::class)
    override fun deserialize(je: JsonElement, type: Type, jdc: JsonDeserializationContext): Date {
        val date = je.asString

        if (date.matches(Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}"))) { // matches format: dd/MM/yyyy
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            return format.parse(date)
        } else {
            return GsonDefaultDateDeserializer().deserialize(je, type, jdc)
        }
    }

}

