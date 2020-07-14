package com.levibostian.extensions

import android.text.TextUtils
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.assertValidEmail(): String? {
    val isValid = !TextUtils.isEmpty(this.text) && Patterns.EMAIL_ADDRESS.matcher(this.text).matches()

    return if (isValid) text.toString() else null
}

/**
 * Note: You must close the keyboard yourself after done.
 */
fun EditText.setDoneButtonListener(listener: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}
