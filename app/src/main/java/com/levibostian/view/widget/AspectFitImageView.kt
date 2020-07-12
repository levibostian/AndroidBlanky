package com.levibostian.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES
import android.text.Editable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView

/**
 * ImageView that has center cropping aspect fit
 */
class AspectFitImageView: AppCompatImageView {

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        adjustViewBounds = true
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

}