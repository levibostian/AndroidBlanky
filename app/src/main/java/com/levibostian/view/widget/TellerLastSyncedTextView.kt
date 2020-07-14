package com.levibostian.view.widget

import android.content.Context
import android.util.AttributeSet
import com.levibostian.extensions.humanReadableTimeAgoSince
import com.levibostian.teller.cachestate.OnlineCacheState

class TellerLastSyncedTextView : UpdateTextView {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(dataState: OnlineCacheState<*>) {
        cancel() // to reset in case this function called multiple times.

        dataState.whenCache { cache, lastSuccessfulFetch, isFetching, justSuccessfullyFetched, errorDuringFetch ->
            super.init {
                "Last synced: ${lastSuccessfulFetch.humanReadableTimeAgoSince}"
            }
        }
    }
}
