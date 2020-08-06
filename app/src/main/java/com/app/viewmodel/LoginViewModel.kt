package com.app.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import io.reactivex.Single

// this is only here for placeholder in case we have login in the app.
class LoginViewModel @ViewModelInject constructor() : ViewModel() {

    fun loginUser(token: String): Single<Unit> {
        return Single.never()
    }
}
