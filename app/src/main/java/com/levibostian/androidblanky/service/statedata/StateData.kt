package com.levibostian.androidblanky.service.statedata

interface StateDataProcessedListener<in DATA_TYPE> {
    fun loadingData()
    fun emptyData()
    fun data(data: DATA_TYPE)
    fun errorFound(error: Throwable)
    fun fetchingFreshData()
    fun finishedFetchingFreshData()
}

open class StateData<DATA_TYPE>(val isLoading: Boolean = false,
                                    val emptyData: Boolean = false,
                                    val data: DATA_TYPE? = null) {

    var latestError: Throwable? = null
    var isFetchingFreshData: Boolean = false
    var doneFetchingFreshData: Boolean = false

    fun errorOccurred(error: Throwable): StateData<DATA_TYPE> {
        latestError = error
        return this
    }

    fun isFetchingFreshData(): StateData<DATA_TYPE> {
        isFetchingFreshData = true
        return this
    }

    fun doneFetchingFreshData(): StateData<DATA_TYPE> {
        isFetchingFreshData = false
        return this
    }

    open fun deliver(listener: StateDataProcessedListener<DATA_TYPE>) {
        when {
            isLoading -> listener.loadingData()
            emptyData -> listener.emptyData()
            isFetchingFreshData -> listener.fetchingFreshData()
            doneFetchingFreshData -> listener.finishedFetchingFreshData()
            data != null -> listener.data(data)
            latestError != null -> listener.errorFound(latestError!!)
        }

        resetTempData()
    }

    private fun resetTempData() { // we only want to deliver some data to the observable once. Once delivered, clear it all.
        isFetchingFreshData = false
        isFetchingFreshData = false
        latestError = null
    }

}
