package com.levibostian.androidblanky.view.ui.fragment

import android.os.Bundle
import android.view.View
import com.artitk.licensefragment.model.License
import com.artitk.licensefragment.model.LicenseID
import com.artitk.licensefragment.model.LicenseType
import com.artitk.licensefragment.support.v4.RecyclerViewLicenseFragment

class LicensesFragment: RecyclerViewLicenseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addLicense(arrayListOf(LicenseID.LICENSE_FRAGMENT))
        addCustomLicense(arrayListOf(
                License(activity, "Android AppCompat", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(activity, "Android Design Support Library", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(activity, "Android RecyclerView v7", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(activity, "Teller-Android", LicenseType.MIT_LICENSE, "2018", "Levi Bostian"),
                License(activity, "Wendy-Android", LicenseType.MIT_LICENSE, "2018", "Levi Bostian"),
                License(activity, "Android Architecture Components", LicenseType.APACHE_LICENSE_20, "2017", "The Android Open Source Project"),
                License(activity, "Room", LicenseType.APACHE_LICENSE_20, "2017", "The Android Open Source Project"),
                License(activity, "Koin", LicenseType.APACHE_LICENSE_20, "2017", "Koin"),
                License(activity, "RxJava2", LicenseType.APACHE_LICENSE_20, "2012", "Netflix, Inc."),
                License(activity, "rx-preferences", LicenseType.APACHE_LICENSE_20, "2014", "Prateek Srivastava"),
                License(activity, "Moshi", LicenseType.APACHE_LICENSE_20, "2015", "Square, Inc."),
                License(activity, "Retrofit2", LicenseType.APACHE_LICENSE_20, "2013", "Square, Inc."),
                License(activity, "OkHttp", LicenseType.APACHE_LICENSE_20, "2012", "Square, Inc."),
                License(activity, "Eventbus", LicenseType.APACHE_LICENSE_20, "2014", "greenrobot"),
                License(activity, "Google Truth", LicenseType.APACHE_LICENSE_20, "2011", "The Android Open Source Project"),
                License(activity, "Mockito", LicenseType.MIT_LICENSE, "2007", "Mockito contributors"),
                License(activity, "Android Testing Support Library", LicenseType.APACHE_LICENSE_20, "2015", "The Android Open Source Project"),
                License(activity, "Espresso", LicenseType.APACHE_LICENSE_20, "2014", "The Android Open Source Project"),
                License(activity, "Fastlane", LicenseType.MIT_LICENSE, "2015-present", "the fastlane authors"),
                License(activity, "Danger", LicenseType.MIT_LICENSE, "2018", "Orta, Felix Krause"),
                License(activity, "Kotlin", LicenseType.APACHE_LICENSE_20, "2010-2017", "JetBrains"),
                License(activity, "AccountAuthenticatorAppCompatActivity", LicenseType.MIT_LICENSE, "2018", "Levi Bostian"),
                License(activity, "Glide", LicenseType.BSD_2_CLAUSE, "2014", "Google"),
                License(activity, "RecyclerViewMatcher", LicenseType.MIT_LICENSE, "2018", "Levi Bostian"),
                License(activity, "WorkManager", LicenseType.APACHE_LICENSE_20, "2018", "Google"),
                License(activity, "Android Debug Database", LicenseType.APACHE_LICENSE_20, "2016", "Amit Shekhar")
        ))
    }
    
}