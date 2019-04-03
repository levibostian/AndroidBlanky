package com.levibostian.androidblanky.view.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * Calls lambda function every second to update the textview text.
 */
class UpdateTextView: TextView {

    private var updateLastSyncedHandler: Handler? = null
    private var updateLastSyncedRunnable: Runnable? = null

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }
    @TargetApi(VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        visibility = View.INVISIBLE
    }

    fun init(update: () -> String) {
        visibility = View.VISIBLE

        updateLastSyncedRunnable?.let { updateLastSyncedHandler?.removeCallbacks(it) }
        updateLastSyncedHandler = Handler()
        updateLastSyncedRunnable = Runnable {
            text = update.invoke()
            updateLastSyncedHandler?.postDelayed(updateLastSyncedRunnable!!, 1000)
        }
        updateLastSyncedHandler?.post(updateLastSyncedRunnable!!)
    }

    // Equivalent to onDestroy()
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        updateLastSyncedRunnable?.let { updateLastSyncedHandler?.removeCallbacks(it) }
    }

}
