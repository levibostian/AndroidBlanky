package com.levibostian.androidblanky.activity

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.curiosityio.andoidviews.activity.BaseFragmentActivity
import com.levibostian.androidblanky.fragment.MainFragment

class MainActivity : BaseFragmentActivity() {

    override fun getInitialFragment(): Fragment? {
        return MainFragment.newInstance()
    }

}