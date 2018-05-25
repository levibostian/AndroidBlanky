package com.levibostian.androidblanky.view.ui.extensions

import android.support.v4.app.Fragment

fun Fragment.closeKeyboard() {
    activity?.closeKeyboard()
}