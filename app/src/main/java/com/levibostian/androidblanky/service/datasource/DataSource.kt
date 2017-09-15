package com.levibostian.androidblanky.service.datasource

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.error.fatal.HttpUnhandledStatusCodeException
import com.levibostian.androidblanky.service.error.nonfatal.RecoverableBadNetworkConnectionException
import com.levibostian.androidblanky.service.repository.Repository
import com.levibostian.androidblanky.service.statedata.StateData
import com.levibostian.androidblanky.service.wrapper.LooperWrapper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.Result
import java.io.IOException
import java.util.*

/**
 * Used to simply get data. I don't care *how* you get data (db, shared prefs), I just need it. The data source decides if data should be stored and retrieved from a server, local db, local storage, cache, whatever.
 *
 * This class is mostly used with the [Repository] class. [Repository]s communicate with [DataSource]s to get data, update data. [DataSource] knows how to fetch new data, save data, when data was last fetched but it does *not*:
 * * Decide when to fetch new data.
 * * Create or manage the state of the data via [StateData].
 *
 * The [DataSource] is *told* when to fetch new data, get it. When views, background jobs, etc. need data, they do *not* use [DataSource]s directly. Create a [Repository] that knows how to sync data and maintain state.
 */
abstract class DataSource<ResultDataType, in FetchFreshDataRequirements, RequestDataType>(open val sharedPreferences: SharedPreferences) {

    /**
     * Key used to save in Shared Preferences the last time new data fetched for Data Source.
     */
    abstract fun lastTimeFreshDataFetchedKey(): String

    /**
     * The last time that [fetchFreshData] was called to fetch new data for this [DataSource]'s data it is coupled with.
     *
     * Use [hasEverFetchedData] as a convenient method to check if data has ever been fetched.
     *
     * @return [Date] of last time data was fetched or null if data has never been fetched.
     */
    open fun lastTimeFreshDataFetched(): Date? {
        val lastTimeDataFetched = sharedPreferences.getLong(lastTimeFreshDataFetchedKey(), 0)
        return if (lastTimeDataFetched == 0L) null else Date(lastTimeDataFetched)
    }

    /**
     * Used to update last time fresh data fetched.
     */
    @SuppressLint("ApplySharedPref")
    open fun updateLastTimeFreshDataFetched(date: Date = Date()) {
        sharedPreferences.edit().putLong(lastTimeFreshDataFetchedKey(), date.time).commit()
    }

    /**
     * Subclasses provide how to fetch fresh data for the specific data it is coupled with. Or fail if an issue.
     */
    abstract fun fetchFreshDataOrFail(requirements: FetchFreshDataRequirements): Single<RequestDataType>

    /**
     * Call to have [DataSource] fetch new data however it does that. More then likely, it will perform a network call to a remote API but it does not need to. It may fetch new data however it wishes.
     *
     * Note: I am using RxJava observable types here because these calls may be async calls. RxJava is a good method of handling that.
     *
     * @return [Completable] that will either complete or error during a fetch.
     */
    open fun fetchFreshData(requirements: FetchFreshDataRequirements): Completable {
        return fetchFreshDataOrFail(requirements)
                .flatMapCompletable { result ->
                    Completable.concatArray(
                            Completable.fromCallable { updateLastTimeFreshDataFetched(Date()) },
                            saveData(result))
                }
                .subscribeOn(Schedulers.io())
    }

    /**
     * Saves data to whatever storage [DataSource] chooses.
     */
    abstract fun saveData(data: RequestDataType): Completable

    /**
     * Creates an [Observable] stream observing the data coupled with this [DataSource].
     */
    abstract fun getData(): Observable<ResultDataType>

    /**
     * Meant to delete database data, shared prefs, etc.
     */
    abstract fun deleteData(): Completable

    /**
     * Reset the data stored in this [DataSource] as if it had never been fetched before. All data will be deleted and [lastTimeFreshDataFetched] will return null the next call.
     */
    @SuppressLint("ApplySharedPref")
    open fun resetData(): Completable {
        return Completable.concatArray(deleteData(),
                Completable.fromCallable {
                    sharedPreferences.edit().putLong(lastTimeFreshDataFetchedKey(), 0).commit()
                })
    }

    /**
     * Cleanup resources created for use with [DataSource].
     */
    abstract fun cleanup()

    /**
     * Has this data ever been fetched before?
     *
     * If you want the actual date and time when data was last fetched, check out [lastTimeFreshDataFetched].
     */
    open fun hasEverFetchedData(): Boolean = lastTimeFreshDataFetched() != null

    /**
     * Used to compare when data was last fetched with a certain date. This is primarily used to determine if data should be refreshed or not. You do not want to fetch new data too often to take advantage of the user's device resources.
     *
     * [Repository]s use this class when data is obtained from a [DataSource] to determine if it should trigger a [fetchFreshData] call.
     */
    open fun isDataOlderThan(date: Date): Boolean {
        if (lastTimeFreshDataFetched() == null) return true
        return lastTimeFreshDataFetched()!! < date
    }

}

/**
 * Extensions optionally used by [DataSource] subclasses to perform actions.
 */
fun <T> Single<Result<T>>.mapApiCallResult(handleStatusCode: (Int) -> Unit): Single<T> {
    return map { result ->
        if (result.isError) {
            val retrofitError = result.error()!!
            if (retrofitError is IOException) {
                // Note: For now, I will mark all requests as recoverable. I am not sure if there are any errors that should fail because my server is not configured correctly or something, but it is risky to guess here what is good and bad so we will leave it at this and assume bad issues will be thrown globally.
                throw RecoverableBadNetworkConnectionException(retrofitError)
            }

            throw retrofitError
        }

        val response = result.response()!!
        if (response.isSuccessful) {
            response.body()!!
        } else {
            handleStatusCode(response.code())

            throw HttpUnhandledStatusCodeException("Sorry, there seems to be an issue. We have been notified. Try again later.")
        }
    }
}

fun <A, B, C> DataSource<A, B, C>.assertMainThread(looperWrapper: LooperWrapper) {
    if (!looperWrapper.isOnUIThread()) throw RuntimeException("Must be on UI thread")
}