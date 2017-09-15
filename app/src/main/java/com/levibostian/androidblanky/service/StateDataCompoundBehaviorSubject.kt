package com.levibostian.androidblanky.service

import com.levibostian.androidblanky.service.statedata.StateData
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class StateDataCompoundBehaviorSubject<DATA_TYPE> {

    private var stateData = StateData<DATA_TYPE>()
    val subject: BehaviorSubject<StateData<DATA_TYPE>> = BehaviorSubject.create()

    fun hasValue(): Boolean = subject.hasValue()

    fun onNextIsLoading() {
        stateData = StateData(isLoading = true)
        subject.onNext(stateData)
    }

    fun onNextEmpty(isFetchingFreshData: Boolean = false) {
        stateData = StateData(emptyData = true)
        if (isFetchingFreshData) stateData = stateData.isFetchingFreshData()
        subject.onNext(stateData)
    }

    fun onNextData(data: DATA_TYPE, isFetchingFreshData: Boolean = false) {
        stateData = StateData(data = data)
        if (isFetchingFreshData) stateData = stateData.isFetchingFreshData()
        subject.onNext(stateData)
    }

    fun onNextCompoundError(error: Throwable) {
        // We don't want a user to have to look at a loading screen after an error occurred therefore, we want to show them an empty view and an error. That's better then an infinite loading screen!
        if (subject.hasValue() && subject.value.isLoading) stateData = StateData(emptyData = true)
        subject.onNext(stateData.errorOccurred(error))
    }

    fun onNextCompoundFetchingFreshData() = subject.onNext(stateData.isFetchingFreshData())

    fun onNextCompoundDoneFetchingFreshData() = subject.onNext(stateData.doneFetchingFreshData())

    fun asObservable(): Observable<StateData<DATA_TYPE>> = subject

}