package com.levibostian.androidblanky.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES
import android.text.Editable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * Convenient way to set different [onTextChanged] listeners for different purposes.
 */
class TextChangedEditText: AppCompatEditText {

    var errorListener: ((text: String) -> String?)? = null

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize()
    }

    val textIfValid: Editable?
        get() {
            return if (error.isNullOrBlank()) super.getText() else null
        }

    private fun initialize() {
    }

    /**
     * @deprecated Use [textIfValid] to be safe.
     */
    @Deprecated(message = "Use textIfValid to be safe.", replaceWith = ReplaceWith("textIfValid", "com.levibostian.androidblanky.view.widget.TextChangedEditText"), level = DeprecationLevel.ERROR)
    override fun getText(): Editable? {
        return super.getText()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        errorListener?.let { errorListener ->
            error = errorListener.invoke(super.getText()?.toString() ?: "") ?: ""
        }

        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

}