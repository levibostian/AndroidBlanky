package com.levibostian.androidblanky.view.ui.widget

import android.widget.LinearLayout
import android.widget.TextView
import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION_CODES
import android.os.Build
import android.view.LayoutInflater
import android.view.Gravity
import android.content.res.TypedArray
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.levibostian.androidblanky.R

open class LoadingView : LinearLayout {

    private lateinit var mLoadingTextView: TextView
    private lateinit var mContext: Context

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }
    @TargetApi(LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context, attrs, defStyleAttr)
    }

    fun initialize(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        mContext = context

        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)

        orientation = VERTICAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setPadding(10, 0, 10, 0)

        mLoadingTextView = findViewById(R.id.loading_textview)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0)

        try {
            setLoadingText(a.getString(R.styleable.LoadingView_loading_loadingText))
            val lightDarkModeInt = a.getInt(R.styleable.LoadingView_loading_lightDarkView, LoadingEmptyLayout.LightDarkMode.LIGHT.mode)
            setLightDarkMode(LoadingEmptyLayout.LightDarkMode.getModeFromInt(lightDarkModeInt))
        } finally {
            a.recycle()
        }
    }

    fun setLightDarkMode(mode: LoadingEmptyLayout.LightDarkMode) {
        if (mode == LoadingEmptyLayout.LightDarkMode.DARK) {
            mLoadingTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        } else if (mode == LoadingEmptyLayout.LightDarkMode.LIGHT) {
            mLoadingTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        }
    }

    fun setLoadingText(loadingText: String?) {
        if (loadingText != null) {
            mLoadingTextView.text = loadingText
        }
    }

}