package com.levibostian.androidblanky.view.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.fragment.app.transaction
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.view.ui.fragment.SettingsFragment
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*

class SettingsActivity: AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_toolbar_fragment_container)
        setSupportActionBar(activity_toolbar_toolbar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.activity_toolbar_fragment_container, SettingsFragment.getInstance())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}