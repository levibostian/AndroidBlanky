package com.levibostian.androidblanky.view.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.accounts.AccountManager
import android.accounts.Account
import android.app.Activity
import android.os.Handler
import androidx.core.content.ContextCompat
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.extensions.plusAssign
import com.levibostian.androidblanky.viewmodel.UserViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_passwordless_email_login.*
import org.koin.android.viewmodel.ext.android.viewModel

abstract class BasePasswordlessEmailLoginActivity: AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModel()

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