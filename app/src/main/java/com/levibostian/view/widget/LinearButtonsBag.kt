package com.levibostian.view.widget


import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.children
import com.levibostian.extensions.random

/**
 * Linear layout buttons bag. A container view that allows you to store and manage X number of buttons.
 */
class LinearButtonsBag: LinearLayout, ButtonsBag {

    private var buttons: MutableMap<ButtonsBagButtonIdentifier, String> = mutableMapOf()

    override val isEmpty: Boolean
        get() = this.count == 0

    override val count: Int
        get() = this.buttons.size

    override lateinit var listener: ButtonsBag.Listener

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    @TargetApi(VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes)

    override fun addButton(id: ButtonsBagButtonIdentifier, @LayoutRes buttonLayout: Int, @IdRes buttonViewId: Int, text: String) {
        removeButton(id)

        val view = LayoutInflater.from(context).inflate(buttonLayout, this, false)
        val button = view.findViewById<Button>(buttonViewId)

        button.text = text

        button.setOnClickListener {
            listener.buttonClicked(id)
        }

        val newTag = String.random(20)
        button.tag = newTag
        buttons[id] = newTag

        addView(button)
    }

    override fun removeAllButtons() {
        buttons.keys.forEach { removeButton(it) }
    }

    override fun removeButton(id: ButtonsBagButtonIdentifier) {
        buttons[id]?.let { buttonTag ->
            children.firstOrNull { it.tag == buttonTag }?.let { existingButton ->
                removeView(existingButton)
            }

            buttons.remove(id)
        }
    }

}
