package com.levibostian.androidblanky.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.levibostian.androidblanky.AppConstants
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.adapter.recyclerview.AboutListAdapter
import com.levibostian.androidblanky.util.ResourceUtil

class AboutFragment : BaseFragment() {

    const private val PRIVACY_POLICY_INDEX: Int = 0
    const private val TERMS_AND_CONDITIONS_INDEX: Int = 1
    const private val OPEN_SOURCE_LICENSES_INDEX: Int = 2
    const private val ABOUT_INDEX: Int = 3

    lateinit private var mAboutListRecyclerView: RecyclerView

    lateinit private var mListListener: ListListener

    interface ListListener {
        fun openSourceLicensesClicked()
    }

}