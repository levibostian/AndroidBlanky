package com.levibostian.androidblanky.view.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import com.levibostian.androidblanky.service.logger.Logger
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.util.InstallReferrerProcessor
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity: BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_toolbar_fragment_container)
        setSupportActionBar(activity_toolbar_toolbar as Toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.activity_toolbar_fragment_container, MainFragment.newInstance())
            }
        }
    }

}