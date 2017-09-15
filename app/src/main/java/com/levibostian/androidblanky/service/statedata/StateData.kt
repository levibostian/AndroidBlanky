package com.levibostian.androidblanky.service.statedata

import com.levibostian.androidblanky.service.StateDataCompoundBehaviorSubject
import com.levibostian.androidblanky.service.datasource.DataSource
import com.levibostian.androidblanky.service.repository.Repository

interface StateDataProcessedListener<in DATA_TYPE> {
    fun loadingData()
    fun emptyData()
    fun data(data: DATA_TYPE)
    fun errorFound(error: Throwable)
    fun fetchingFreshData()
    fun finishedFetchingFreshData()
}

/**
 * Data in apps are in 1 of 3 different types of state:
 *
 * 1. Data does not exist. It has never been obtained before.
 * 2. It is empty. Data has been obtained before, but there is none.
 * 3. Data exists.
 *
 * This class takes in a type of data to keep state on via generic [DATA_TYPE] and it maintains the state of that data.
 *
 * Along with the 3 different states data could be in, there are temporary states that data could also be in.
 *
 * * An error occurred with that data.
 * * Fresh data is being fetched for this data. It may be updated soon.
 *
 * The 3 states listed above empty, data, loading are all permanent. Data is 1 of those 3 at all times. Data has this error or fetching status temporarily until someone calls [deliver] one time and then those temporary states are deleted.
 *
 * This class is used in companion with [Repository] and [StateDataCompoundBehaviorSubject] to maintain the state of data to deliver to someone observing.
 */
open class StateData<DATA_TYPE>(val isLoading: Boolean = false,
                                val emptyData: Boolean = false,
                                val data: DATA_TYPE? = null) {

    var latestError: Throwable? = null
    var isFetchingFreshData: Boolean = false
    var doneFetchingFreshData: Boolean = false

    /**
     * Tag on an error to this data. Errors could be an error fetching fresh data or reading data off the device. The errors should have to deal with this data, not some generic error encountered in the app.
     */
    fun errorOccurred(error: Throwable): StateData<DATA_TYPE> {
        latestError = error
        return this
    }

    /**
     * Set the status of this data as fetching fresh data.
     */
    fun isFetchingFreshData(): StateData<DATA_TYPE> {
        isFetchingFreshData = true
        return this
    }

    /**
     * Set the status of this data as done fetching fresh data.
     */
    fun doneFetchingFreshData(): StateData<DATA_TYPE> {
        isFetchingFreshData = false
        return this
    }

    /**
     * This is usually used in the UI of an app to display data to a user.
     *
     * Using this function, you can get the state of the data as well as handle errors that may have happened with data (during fetching fresh data or reading the data off the device) or get the status of fetching fresh new data.
     *
     * Call this function when an instance of [StateData] is given to you.
     */
    open fun deliver(listener: StateDataProcessedListener<DATA_TYPE>) {
        if (isLoading) listener.loadingData()
        if (emptyData) listener.emptyData()
        if (isFetchingFreshData) listener.fetchingFreshData()
        if (doneFetchingFreshData) listener.finishedFetchingFreshData()
        if (data != null) listener.data(data)
        if (latestError != null) listener.errorFound(latestError!!)
//        when {
//            isLoading -> listener.loadingData()
//            emptyData -> listener.emptyData()
//            isFetchingFreshData -> listener.fetchingFreshData()
//            doneFetchingFreshData -> listener.finishedFetchingFreshData()
//            data != null -> listener.data(data)
//            latestError != null -> listener.errorFound(latestError!!)
//        }

        resetTempData()
    }

    private fun resetTempData() { // we only want to deliver some data to the observable once. Once delivered, clear it all.
        isFetchingFreshData = false
        isFetchingFreshData = false
        latestError = null
    }

}
