package com.levibostian.androidblanky.view.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.Gravity
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.levibostian.androidblanky.R

open class EmptyView : LinearLayout {

    private lateinit var mContext: Context

    private lateinit var mEmptyImageView: ImageView
    private lateinit var mEmptyTextView: TextView

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }
    @TargetApi(VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context, attrs, defStyleAttr)
    }

    fun initialize(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        mContext = context

        LayoutInflater.from(context).inflate(R.layout.view_empty, this, true)

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setPadding(10, 0, 10, 0)

        mEmptyImageView = findViewById(R.id.empty_view_imageview)
        mEmptyTextView = findViewById(R.id.empty_view_textview)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyView, 0, 0)

        try {
            setEmptyImageView(a.getResourceId(R.styleable.EmptyView_emptyView_emptyImageRes, -1))
            setEmptyText(a.getString(R.styleable.EmptyView_emptyView_emptyText))
            val lightDarkModeInt = a.getInt(R.styleable.EmptyView_emptyView_lightDarkView, LoadingEmptyLayout.LightDarkMode.LIGHT.mode)
            setLightDarkMode(LoadingEmptyLayout.LightDarkMode.getModeFromInt(lightDarkModeInt))
        } finally {
            a.recycle()
        }
    }

    fun setLightDarkMode(mode: LoadingEmptyLayout.LightDarkMode) {
        if (mode == LoadingEmptyLayout.LightDarkMode.DARK) {
            mEmptyTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        } else if (mode == LoadingEmptyLayout.LightDarkMode.LIGHT) {
            mEmptyTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        }
    }

    fun setEmptyImageView(imageRes: Int) {
        if (imageRes != -1) {
            mEmptyImageView.setImageResource(imageRes)
            mEmptyImageView.visibility = VISIBLE
        }
    }

    fun setEmptyText(loadingText: String?) {
        if (loadingText != null) {
            mEmptyTextView.text = loadingText
        }
    }

}