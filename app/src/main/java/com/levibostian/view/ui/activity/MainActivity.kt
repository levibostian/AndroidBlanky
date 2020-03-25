package com.levibostian.view.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.levibostian.R
import com.levibostian.service.DataDestroyer
import com.levibostian.service.event.LogoutUserEvent
import com.levibostian.service.logger.Logger
import com.levibostian.service.manager.UserManager
import com.levibostian.service.util.InstallReferrerProcessor
import com.levibostian.view.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: BaseActivity() {

    private val navController: NavController
        get() = findNavController(R.id.nav_host_fragment)

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        (toolbar as Toolbar).setupWithNavController(navController, AppBarConfiguration(navController.graph))
    }

}