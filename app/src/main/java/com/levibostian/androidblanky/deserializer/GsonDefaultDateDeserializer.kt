package com.levibostian.androidblanky.deserializer

import com.google.gson.*
import com.google.gson.internal.bind.util.ISO8601Utils
import java.lang.reflect.Type
import java.sql.Timestamp
import java.text.DateFormat
import java.text.ParseException
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This type adapter supports three subclasses of date: Date, Timestamp, and
 * java.sql.Date.

 * @author Inderjeet Singh
 * *
 * @author Joel Leitch
 */
class GsonDefaultDateDeserializer @JvmOverloads constructor(

        private val enUsFormat: DateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US), private val localFormat: DateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT)) : JsonSerializer<Date>, JsonDeserializer<Date> {

    constructor(datePattern: String) : this(SimpleDateFormat(datePattern, Locale.US), SimpleDateFormat(datePattern)) {
    }

    constructor(style: Int) : this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style)) {
    }

    constructor(dateStyle: Int, timeStyle: Int) : this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US),
            DateFormat.getDateTimeInstance(dateStyle, timeStyle)) {
    }

    // These methods need to be synchronized since JDK DateFormat classes are not thread-safe
    // See issue 162
    override fun serialize(src: Date, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        synchronized(localFormat) {
            val dateFormatAsString = enUsFormat.format(src)
            return JsonPrimitive(dateFormatAsString)
        }
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        if (json !is JsonPrimitive) {
            throw JsonParseException("The date should be a string value")
        }
        val date = deserializeToDate(json)
        if (typeOfT === Date::class.java) {
            return date
        } else if (typeOfT === Timestamp::class.java) {
            return Timestamp(date.time)
        } else if (typeOfT === java.sql.Date::class.java) {
            return java.sql.Date(date.time)
        } else {
            throw IllegalArgumentException(javaClass.simpleName + " cannot deserialize to " + typeOfT)
        }
    }

    private fun deserializeToDate(json: JsonElement): Date {
        synchronized(localFormat) {
            try {
                return localFormat.parse(json.asString)
            } catch (ignored: ParseException) {
            }

            try {
                return enUsFormat.parse(json.asString)
            } catch (ignored: ParseException) {
            }

            try {
                return ISO8601Utils.parse(json.asString, ParsePosition(0))
            } catch (e: ParseException) {
                throw JsonSyntaxException(json.asString, e)
            }

        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(GsonDefaultDateDeserializer::class.java.simpleName)
        sb.append('(').append(localFormat.javaClass.simpleName).append(')')
        return sb.toString()
    }

}



