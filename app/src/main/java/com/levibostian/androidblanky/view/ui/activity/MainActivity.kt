package com.levibostian.androidblanky.view.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.R.id.activity_toolbar_toolbar
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    @Inject lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MainApplication).component.inject(this)

        setContentView(R.layout.activity_toolbar_fragment_container)
        setSupportActionBar(activity_toolbar_toolbar as Toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.activity_toolbar_fragment_container, MainFragment.newInstance()).commit()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: LogoutUserEvent) {
        userManager.logout()
        startActivity(LaunchActivity.getIntent(this))
        finish()
    }

}