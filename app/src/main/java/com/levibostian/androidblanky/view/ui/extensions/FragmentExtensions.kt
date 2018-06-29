package com.levibostian.androidblanky.view.ui.extensions

import androidx.fragment.app.Fragment

fun Fragment.closeKeyboard() {
    activity?.closeKeyboard()
}