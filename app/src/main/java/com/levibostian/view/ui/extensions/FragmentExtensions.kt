package com.levibostian.view.ui.extensions

import androidx.fragment.app.Fragment

fun Fragment.closeKeyboard() {
    activity?.closeKeyboard()
}

fun <LISTENER> Fragment.getListener(): LISTENER? {
    return (activity as? LISTENER) ?: (parentFragment as? LISTENER)
}