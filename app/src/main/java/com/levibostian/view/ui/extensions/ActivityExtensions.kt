package com.levibostian.view.ui.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.levibostian.extensions.closeKeyboardOnThisFocusedView

fun Activity.closeKeyboard() {
    this.currentFocus?.closeKeyboardOnThisFocusedView(this)
}

fun Activity.browse(uri: Uri) {
    startActivity(Intent(Intent.ACTION_VIEW, uri))
}
