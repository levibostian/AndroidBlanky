package com.levibostian.service.util

import com.levibostian.Env
import com.levibostian.extensions.appVersion


object StringReplaceUtil {

    fun replace(string: String, values: List<Pair<String, String>>? = null): String {
        @Suppress("NAME_SHADOWING") var string = string

        // Default keys and values to replace
        string = string.replace(StringReplaceTemplate.PLATFORM.pattern, "Android")
                .replace(StringReplaceTemplate.APP_VERSION.pattern, Env.appVersion)
                .replace(StringReplaceTemplate.APP_NAME.pattern, Env.appName)

        values?.forEach { pair ->
            string = string.replace("{{${pair.first}}}", pair.second)
        }

        return string
    }
}

enum class StringReplaceTemplate {
    PLATFORM {
        override val pattern: String = "{{platform}}"
    },
    APP_VERSION {
        override val pattern: String = "{{app_version}}"
    },
    APP_NAME {
        override val pattern: String = "{{app_name}}"
    };

    abstract val pattern: String
}