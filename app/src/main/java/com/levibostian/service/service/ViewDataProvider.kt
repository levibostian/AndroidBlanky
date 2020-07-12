package com.levibostian.service.service

import android.content.Context
import com.levibostian.R
import com.levibostian.boquila.RemoteConfigAdapter
import com.levibostian.view.ui.fragment.MainFragment
import javax.inject.Inject

class ViewDataProvider @Inject constructor(private val context: Context, private val remoteConfig: RemoteConfigAdapter) {

    val mainFragment: MainFragment.ViewData = remoteConfig.getValue("main_fragment_viewdata", MainFragment.ViewData::class.java, MainFragment.ViewData(context.getString(R.string.loading_repos)))

}
