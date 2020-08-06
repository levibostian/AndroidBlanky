package com.app.view.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes

class OnOffButton : androidx.appcompat.widget.AppCompatImageButton {

    @DrawableRes var onImage: Int? = null
    @DrawableRes var offImage: Int? = null

    var isOn: Boolean = false
        set(value) {
            field = value

            setImageResource(if (isOn) onImage!! else offImage!!)
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}
