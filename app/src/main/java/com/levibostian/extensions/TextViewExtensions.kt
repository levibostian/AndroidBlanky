package com.levibostian.extensions

import android.view.View
import android.widget.TextView

var TextView.textOrHide: String?
    get() = text?.toString()
    set(value) {
        visibility = if (value == null) View.GONE else View.VISIBLE

        text = value
    }