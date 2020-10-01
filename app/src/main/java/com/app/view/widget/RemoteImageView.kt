package com.app.view.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import coil.api.load
import coil.transform.CircleCropTransformation

/**
 * Display images from a URL with ease!
 *
 * This View is an encapsulated way to load images in your app. All you need to do is call the [load] function. That's it! We do the rest for you.
 *
 * Note: This file uses the open source library Coil https://coil-kt.github.io/coil/. We encapsulate this library into this 1 file so that we can easily swap to another library in the future and we only need to modify 1 single file!
 */
class RemoteImageView : AppCompatImageView {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
    }

    fun load(uri: Uri, crossfadeAfterLoad: Boolean = true, @DrawableRes placeHolder: Int? = null, centerCircleCrop: Boolean = false) {
        this.load(uri) {
            crossfade(crossfadeAfterLoad)

            placeHolder?.let {
                placeholder(it)
            }

            if (centerCircleCrop) {
                transformations(listOf(CircleCropTransformation()))
            }
        }
    }
}
