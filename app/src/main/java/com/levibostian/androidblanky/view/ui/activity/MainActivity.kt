package com.levibostian.androidblanky.view.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.R.id.activity_toolbar_toolbar
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_toolbar_fragment_container)
        setSupportActionBar(activity_toolbar_toolbar as Toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.activity_toolbar_fragment_container, MainFragment.newInstance()).commit()
        }
    }

}