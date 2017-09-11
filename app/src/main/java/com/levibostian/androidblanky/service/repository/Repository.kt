package com.levibostian.androidblanky.service.repository

import android.arch.lifecycle.ViewModel
import com.levibostian.androidblanky.service.statedata.StateData
import com.levibostian.androidblanky.service.datasource.DataSource

/**
 * Used to:
 *
 * 1. Create Observables for Android architecture [ViewModel] instances to pass to Views.
 * 2. Obtains data from [DataSource] instance(s) to get data to pass to [ViewModel] instances.
 * 3. Determines when data from [DataSource] is old and needs to be updated.
 * 4. Determines what state data is in via [StateData] to pass to Views via [ViewModel].
 */
interface Repository {

    fun cleanup()

}