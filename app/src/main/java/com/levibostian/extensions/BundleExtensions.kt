package com.levibostian.extensions

import android.os.Bundle
import androidx.core.os.bundleOf

fun Map<String, Any>.toBundle(): Bundle {
    val pairs = this.map {
        Pair(it.key, it.value)
    }.toTypedArray()

    return bundleOf(*pairs)
}