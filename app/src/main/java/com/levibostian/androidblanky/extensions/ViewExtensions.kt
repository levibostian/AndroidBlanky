package com.levibostian.androidblanky.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

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