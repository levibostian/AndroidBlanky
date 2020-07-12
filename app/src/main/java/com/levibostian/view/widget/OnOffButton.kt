package com.levibostian.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.DrawableRes

class FavoriteButton: androidx.appcompat.widget.AppCompatImageButton {

    @DrawableRes var onImage: Int? = null
    @DrawableRes var offImage: Int? = null

    var isOn: Boolean = false
        set(value) {
            field = value

            setImageResource(if (isOn) onImage!! else offImage!!)
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

}