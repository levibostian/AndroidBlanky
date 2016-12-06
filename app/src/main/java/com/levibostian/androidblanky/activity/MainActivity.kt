package com.levibostian.androidblanky.activity

import android.app.Fragment
import com.curiosityio.andoidviews.activity.BaseActivity
import com.levibostian.androidblanky.fragment.MainFragment

class MainActivity : BaseActivity() {

    override fun getInitialFragment(): Fragment? {
        return MainFragment.newInstance()
    }

}