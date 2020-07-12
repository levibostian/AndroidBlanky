package com.levibostian.extensions

import android.view.View
import android.widget.ScrollView

fun ScrollView.scrollToView(view: View) {
    post(Runnable { scrollTo(0, view.bottom) })
}