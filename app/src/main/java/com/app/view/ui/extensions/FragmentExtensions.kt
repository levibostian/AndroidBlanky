package com.app.view.ui.extensions

import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.R

fun Fragment.closeKeyboard() {
    activity?.closeKeyboard()
}

fun <LISTENER> Fragment.getListener(): LISTENER? {
    return (activity as? LISTENER) ?: (parentFragment as? LISTENER)
}

fun Fragment.browse(uri: Uri) {
    activity?.browse(uri)
}

fun Fragment.showError(message: String) {
    AlertDialog.Builder(requireActivity())
        .setMessage(message)
        .setNegativeButton(R.string.ok, null) // null listener dismisses the dialog
        .show()
}

fun Fragment.hideToolbar() {
    (activity as AppCompatActivity).supportActionBar?.hide()
}

fun Fragment.showToolbar() {
    (activity as AppCompatActivity).supportActionBar?.show()
}

/**
 * Note: You do not want to use this to set the Toolbar title. Use: https://stackoverflow.com/a/55701078/1486374
 */
// var Fragment.title: String
//    get() = (requireActivity() as AppCompatActivity).title.toString()
//    set(value) {
//        (requireActivity() as AppCompatActivity).title = value
//    }
