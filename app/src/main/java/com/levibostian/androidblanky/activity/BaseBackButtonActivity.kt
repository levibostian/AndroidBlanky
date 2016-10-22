package com.levibostian.androidblanky.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.levibostian.androidblanky.R

abstract class BaseBackButtonActivity: BaseActivity() {

    protected lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base_back_button)

        mToolbar = findViewById(R.id.back_toolbar) as Toolbar

        setupViews()
    }

    override fun onResume() {
        super.onResume()

        setTitle(getScreenTitle())
    }

    protected fun setFragmentInFragmentContainer(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
    }

    protected fun replaceFragmentInFragmentContainer(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }

        fragmentTransaction.commit()
    }

    protected abstract fun getScreenTitle(): Int

    private fun setupViews() {
        setSupportActionBar(mToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}