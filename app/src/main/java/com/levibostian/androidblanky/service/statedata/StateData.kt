package com.levibostian.androidblanky.service.statedata

abstract class StateData<in DATA_TYPE>(state: StateData.State, repos: DATA_TYPE?, error: Throwable?) {

    enum class State {
        LOADING,
        EMPTY,
        ERROR,
        DATA
    }

}
