package com.levibostian.androidblanky.activity

import android.os.Bundle
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.fragment.MainFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setupFragment()
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, MainFragment.newInstance()).commit()
    }

}