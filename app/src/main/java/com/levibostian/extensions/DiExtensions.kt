package com.levibostian.extensions

import android.app.Activity
import android.app.Service
import androidx.fragment.app.Fragment
import com.levibostian.di.AppGraph
import com.levibostian.view.ui.MainApplication

fun Activity.onCreateDiGraph(): AppGraph {
    return (application as MainApplication).appComponent
}

fun Fragment.onAttachDiGraph(): AppGraph {
    return (activity!!.application as MainApplication).appComponent
}

fun Service.onCreateDiGraph(): AppGraph {
    return (application as MainApplication).appComponent
}
