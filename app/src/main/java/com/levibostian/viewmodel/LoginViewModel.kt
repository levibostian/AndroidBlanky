package com.levibostian.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.Single
import javax.inject.Inject

// this is only here for placeholder in case we have login in the app.
class LoginViewModel @Inject constructor() : ViewModel() {

    fun loginUser(token: String): Single<Unit> {
        return Single.never()
    }
}
