package com.levibostian.androidblanky.view.ui.activity

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.artitk.licensefragment.RecyclerViewLicenseFragment
import com.artitk.licensefragment.model.License
import com.artitk.licensefragment.model.LicenseID
import com.artitk.licensefragment.model.LicenseType
import com.levibostian.androidblanky.R
import kotlinx.android.synthetic.main.activity_toolbar_fragment_container.*

class LicensesActivity: AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, LicensesActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_toolbar_fragment_container)
        setSupportActionBar(activity_toolbar_toolbar as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.activity_toolbar_fragment_container, getLicensesFragment()).commit()
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

    private fun getLicensesFragment(): Fragment {
        val fragment = RecyclerViewLicenseFragment.newInstance()
        fragment.addLicense(arrayListOf(LicenseID.LICENSE_FRAGMENT))
        fragment.addCustomLicense(arrayListOf(
                License(this, "Android AppCompat", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(this, "Android Design Support Library", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(this, "Android RecyclerView v7", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(this, "Teller-Android", LicenseType.MIT_LICENSE, "2018", "Levi Bostian"),
                License(this, "Wendy-Android", LicenseType.MIT_LICENSE, "2018", "Levi Bostian"),
                License(this, "Android Architecture Components", LicenseType.APACHE_LICENSE_20, "2017", "The Android Open Source Project"),
                License(this, "Room", LicenseType.APACHE_LICENSE_20, "2017", "The Android Open Source Project"),
                License(this, "Dagger 2", LicenseType.APACHE_LICENSE_20, "2012", "Google"),
                License(this, "RxJava2", LicenseType.APACHE_LICENSE_20, "2012", "Netflix, Inc."),
                License(this, "rx-preferences", LicenseType.APACHE_LICENSE_20, "2014", "Prateek Srivastava"),
                License(this, "Moshi", LicenseType.APACHE_LICENSE_20, "2015", "Square, Inc."),
                License(this, "Retrofit2", LicenseType.APACHE_LICENSE_20, "2013", "Square, Inc."),
                License(this, "OkHttp", LicenseType.APACHE_LICENSE_20, "2012", "Square, Inc."),
                License(this, "Eventbus", LicenseType.APACHE_LICENSE_20, "2014", "greenrobot"),
                License(this, "Timber", LicenseType.APACHE_LICENSE_20, "2013", "Jake Wharton"),
                License(this, "Google Truth", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(this, "Mockito", LicenseType.MIT_LICENSE, "2007", "Mockito contributors"),
                License(this, "Android Testing Support Library", LicenseType.APACHE_LICENSE_20, "2015", "The Android Open Source Project"),
                License(this, "Espresso", LicenseType.APACHE_LICENSE_20, "2014", "The Android Open Source Project"),
                License(this, "Fastlane", LicenseType.MIT_LICENSE, "2015-present", "the fastlane authors"),
                License(this, "Danger", LicenseType.MIT_LICENSE, "2018", "Orta, Felix Krause"),
                License(this, "Kotlin", LicenseType.APACHE_LICENSE_20, "2010-2017", "JetBrains")
        ))
        return fragment
    }

}