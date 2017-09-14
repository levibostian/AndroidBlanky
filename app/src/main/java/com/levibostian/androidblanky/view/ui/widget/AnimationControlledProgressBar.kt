package com.levibostian.androidblanky.view.ui.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.widget.ProgressBar

class AnimationControlledProgressBar : ProgressBar {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun setIndeterminateDrawable(drawable: Drawable) {
        super.setIndeterminateDrawable(if (hideIndeterminateDrawable()) null else drawable)
    }

    private fun hideIndeterminateDrawable(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f
    }

}