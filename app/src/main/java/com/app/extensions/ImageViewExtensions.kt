package com.app.extensions

import android.widget.ImageView

fun ImageView.aspectFit() {
    adjustViewBounds = true
    scaleType = ImageView.ScaleType.CENTER_CROP
}
