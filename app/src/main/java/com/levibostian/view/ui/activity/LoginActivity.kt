package com.levibostian.view.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.levibostian.R
import com.levibostian.extensions.onCreateDiGraph
import com.levibostian.extensions.plusAssign
import com.levibostian.service.logger.Logger
import com.levibostian.viewmodel.LoginViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity: AppCompatActivity() {

    @Inject lateinit var logger: Logger
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val loginViewModel by viewModels<LoginViewModel> { viewModelFactory }

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
        onCreateDiGraph().inject(this)
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