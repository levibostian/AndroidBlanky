package com.levibostian.androidblanky.service.repository

import android.arch.lifecycle.ViewModel
import com.levibostian.androidblanky.service.StateDataCompoundBehaviorSubject
import com.levibostian.androidblanky.service.statedata.StateData
import com.levibostian.androidblanky.service.datasource.DataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import java.util.*


/**
 * Used to:
 *
 * * Create Observables for Android architecture [ViewModel] instances to pass to Views.
 * * One time sync of data. Great for background jobs to run and sync data.
 * * Obtains data from [DataSource] instance(s) to get data to pass to [ViewModel] instances.
 * * Determines when data from [DataSource] is old and needs to be updated.
 * * Determines what state the data is in via [StateData] to pass to Views via [ViewModel].
 */
abstract class Repository(val composite: CompositeDisposable) {

    /**
     * Collection of [BehaviorSubject] instances (wrapped in [StateDataCompoundBehaviorSubject]). Each instance of [StateDataCompoundBehaviorSubject] is mapped to it's companion [DataSource].
     *
     * Create a [StateDataCompoundBehaviorSubject] for a [DataSource] by calling [getSubjectOrCreate] or [startObservingStateOfData].
     */
    private val subjects: HashMap<String, StateDataCompoundBehaviorSubject<Any>> = HashMap()

    /**
     * Perform an ongoing sync. Begin taking [DataSource]s, if any, and start observing them via [startObservingStateOfData].
     */
    abstract fun startObservingStateOfData()

    /**
     * Repository keeps track of [StateDataCompoundBehaviorSubject] subjects (aka: [Observable]s) to watch the state of data. Use this function to get the subject mapped to a particular [DataSource].
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun <A, B, C> getSubjectOrCreate(dataSource: DataSource<A, B, C>): StateDataCompoundBehaviorSubject<A> {
        val subject = subjects[dataSource.javaClass.simpleName]
        if (subject != null) return subject as StateDataCompoundBehaviorSubject<A>

        val newSubject = StateDataCompoundBehaviorSubject<A>()
        subjects[dataSource.javaClass.simpleName] = newSubject as StateDataCompoundBehaviorSubject<Any>

        return newSubject
    }

    /**
     * Convenient method to communicate with a [DataSource] to start observing the [DataSource] via [DataSource.getData] and then maintain the state of that data via an instance of [StateData].
     *
     * Call [getSubjectOrCreate] to get an [Observable] you can observe the [StateData] for this particular [DataSource] you pass in here as a parameter.
     */
    protected open fun <A, B, C> startObservingStateOfData(dataSource: DataSource<A, B, C>, minimumAllowedDataOld: Date, isDataEmpty: (data: A) -> Boolean, handleObservable: ((Observable<A>) -> Observable<A>)? = null) {
        val subject = getSubjectOrCreate(dataSource)
        if (subject.hasValue()) return

        var observeDataObservable = dataSource.getData()
        observeDataObservable = handleObservable?.invoke(observeDataObservable) ?: observeDataObservable

        composite += observeDataObservable
                .filter {
                    val hasEverFetchedData = dataSource.hasEverFetchedData()
                    if (!hasEverFetchedData) {
                        subject.onNextIsLoading()
                        sync().subscribe({
                        }, { error -> subject.onNextCompoundError(error) })
                    }
                    hasEverFetchedData
                }
                .subscribe({ data ->
                    val needToFetchFreshData = dataSource.isDataOlderThan(minimumAllowedDataOld)

                    if (isDataEmpty(data)) subject.onNextEmpty(isFetchingFreshData = needToFetchFreshData)
                    else subject.onNextData(data, isFetchingFreshData = needToFetchFreshData)

                    if (needToFetchFreshData) {
                        sync().subscribe({
                            subject.onNextCompoundDoneFetchingFreshData()
                        }, { error -> subject.onNextCompoundError(error) })
                    }
                })
    }

    /**
     * Handy to call from a background job to sync data this [Repository] observes. One time sync (if needed)
     */
    abstract fun sync(): Completable

    /**
     * Cleanup resources. Call [DataSource.cleanup] functions as well.
     */
    abstract fun cleanupResources()

    fun cleanup() {
        composite.clear()
        cleanupResources()
    }

}