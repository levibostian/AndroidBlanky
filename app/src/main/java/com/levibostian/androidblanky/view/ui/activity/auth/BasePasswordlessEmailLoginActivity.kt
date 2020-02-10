package com.levibostian.androidblanky.view.ui.activity.auth

import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.extensions.plusAssign
import com.levibostian.androidblanky.viewmodel.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BasePasswordlessEmailLoginActivity: AppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val userViewModel by viewModels<UserViewModel> { viewModelFactory }

    private val compositeDisposable = CompositeDisposable()

    protected fun getPasswordlessToken(email: String) {
        compositeDisposable += userViewModel.loginPasswordlessEmail(email)
                .subscribe { response ->
                    response.failure?.let {
                        getPasswordlessTokenError(it)
                    } ?: kotlin.run {
                        getPasswordlessTokenSuccess()
                    }
                }

        Handler().postDelayed({ // Pretending that we got our email and auth token back from the server.
            getPasswordlessTokenSuccess()
        }, 4000)
    }

    abstract fun getPasswordlessTokenSuccess()

    abstract fun getPasswordlessTokenError(error: Throwable)

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }

}