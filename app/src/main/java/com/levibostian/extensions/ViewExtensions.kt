package com.levibostian.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.fadeOutAnimation(duration: Long, endVisibility: Int = View.GONE) {
    apply {
        alpha = 1f
        visibility = View.VISIBLE

        animate()
            .alpha(0f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = endVisibility
                }
            })
            .start()
    }
}

fun View.fadeInAnimation(duration: Long, startVisibility: Int = View.GONE) {
    apply {
        alpha = 0f
        visibility = startVisibility

        animate()
            .alpha(1f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    visibility = View.VISIBLE
                }
            })
            .start()
    }
}

fun View.closeKeyboardOnThisFocusedView(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}
