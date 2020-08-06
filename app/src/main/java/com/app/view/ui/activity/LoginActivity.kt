package com.app.view.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.R
import com.app.extensions.plusAssign
import com.app.service.logger.Logger
import com.app.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject lateinit var logger: Logger
    private val loginViewModel: LoginViewModel by viewModels()

    companion object {
        private val PASSWORDLESS_TOKEN_KEY = "LoginActivity.PASSWORDLESS_TOKEN_KEY"

        fun getIntent(context: Context, passwordlessToken: String): Intent = Intent(context, LoginActivity::class.java).apply {
            putExtra(PASSWORDLESS_TOKEN_KEY, passwordlessToken)
        }
    }

    private val compositeDisposable = CompositeDisposable()

    enum class SwapperViews {
        LOADING,
        ERROR;
    }

    private val passwordlessToken: String
        get() = intent.getStringExtra(PASSWORDLESS_TOKEN_KEY)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()

        loading_view.apply {
            title = getString(R.string.logging_into_app_title)
        }

        swapper_view.viewMap = mapOf(
            Pair(SwapperViews.LOADING.name, loading_view),
            Pair(SwapperViews.ERROR.name, error_view)
        )

        swapper_view.swapTo(SwapperViews.LOADING.name) {}

        compositeDisposable += loginViewModel.loginUser(passwordlessToken)
            .subscribe { _ ->
                setResult(Activity.RESULT_OK)
                finish()
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }
}
