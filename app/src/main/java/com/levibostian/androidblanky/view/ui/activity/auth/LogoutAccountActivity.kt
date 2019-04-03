package com.levibostian.androidblanky.view.ui.activity.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.view.ui.dialog.AreYouSureLogoutWendyDialogFragment
import com.levibostian.wendy.service.Wendy
import kotlinx.android.synthetic.main.activity_password_token.*
import org.koin.android.ext.android.inject

class LogoutAccountActivity: AppCompatActivity(), AreYouSureLogoutWendyDialogFragment.Listener {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, LogoutAccountActivity::class.java)
    }

    private val dataDestroyer: DataDestroyer by inject()

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        setContentView(R.layout.activity_logout_account)
        setupViews()
    }

    private fun setupViews() {
        act_password_token_loading_view.loadingTextView!!.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }

    override fun onStart() {
        super.onStart()

        continueLogoutProcess()
    }

    private fun continueLogoutProcess() {
        if (assertReadyToLogout()) {
            dataDestroyer.destroyAll {
                logoutProcessComplete(cancelLogout = false)
            }
        }
    }

    private fun assertReadyToLogout(): Boolean {
        return assertPendingTasksComplete()
    }

    private fun assertPendingTasksComplete(): Boolean {
        return if (Wendy.shared.getAllTasks().isNotEmpty()) {
            AreYouSureLogoutWendyDialogFragment.getInstance().show(supportFragmentManager, null)

            false
        } else {
            true
        }
    }

    override fun logout() {
        continueLogoutProcess()
    }

    override fun cancel() {
        logoutProcessComplete(cancelLogout = true)
    }

    private fun logoutProcessComplete(cancelLogout: Boolean) {
        setResult(if (cancelLogout) Activity.RESULT_CANCELED else Activity.RESULT_OK)
        finish()
    }

}