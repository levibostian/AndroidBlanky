package com.levibostian.view.widget

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
open class UpdateTextView: androidx.appcompat.widget.AppCompatTextView {

    private var updateLastSyncedHandler: Handler? = null
    private var updateLastSyncedRunnable: Runnable? = null

    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        cancel()
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

    open fun cancel() {
        visibility = View.INVISIBLE
        updateLastSyncedRunnable?.let { updateLastSyncedHandler?.removeCallbacks(it) }
    }

    // Equivalent to onDestroy()
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        cancel()
    }

}
