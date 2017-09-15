package com.levibostian.androidblanky.service.datasource

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Looper
import com.levibostian.androidblanky.service.error.fatal.HttpUnhandledStatusCodeException
import com.levibostian.androidblanky.service.error.nonfatal.HttpUnsuccessfulStatusCodeException
import com.levibostian.androidblanky.service.error.nonfatal.RecoverableBadNetworkConnectionException
import com.levibostian.androidblanky.service.error.nonfatal.UserErrorException
import com.levibostian.androidblanky.service.repository.Repository
import com.levibostian.androidblanky.service.wrapper.LooperWrapper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import khronos.Dates
import khronos.Duration
import khronos.minus
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result
import java.io.IOException
import java.util.*

/**
 * Used to simply get data. I don't care *how* you get data, I just need it. The data source decides if data should be stored and retrieved from a server, local db, local storage, cache, whatever.
 *
 * This class is mostly used with the [Repository] class.
 */
abstract class DataSource<ResultDataType, in FetchNewDataRequirements, in RequestDataType>(open val sharedPreferences: SharedPreferences) {

    /**
     * Key used to save in Shared Preferences the last time new data fetched for Data Source.
     */
    abstract fun lastTimeNewDataFetchedKey(): String

    fun lastTimeNewDataFetched(): Date? {
        val lastTimeDataFetched = sharedPreferences.getLong(lastTimeNewDataFetchedKey(), 0)
        return if (lastTimeDataFetched == 0L) null else Date(lastTimeDataFetched)
    }

    /**
     * I am using RxJava observable types here because these calls may be async calls. RxJava is a good method of handling that.
     */
    abstract fun fetchNewData(requirements: FetchNewDataRequirements): Completable

    /**
     * Saves data to whatever storage [DataSource] chooses. It may decide to return an identifier for the source if there is one.
     */
    abstract fun saveData(data: RequestDataType): Completable

    abstract fun getData(): Observable<ResultDataType>

    /**
     * Meant to delete database data, shared prefs, etc.
     */
    abstract fun deleteData(): Completable

    @SuppressLint("ApplySharedPref")
    fun resetData(): Completable {
        return Completable.concatArray(deleteData(),
                Completable.fromCallable {
                    sharedPreferences.edit().putLong(lastTimeNewDataFetchedKey(), 0).commit()
                })
    }

    abstract fun cleanup()

    fun hasEverFetchedData(): Boolean = lastTimeNewDataFetched() != null

    fun isDataOlderThan(date: Date): Boolean {
        if (lastTimeNewDataFetched() == null) return true
        return lastTimeNewDataFetched()!! < date
    }

}

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