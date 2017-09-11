package com.levibostian.androidblanky.service.datasource

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import java.util.*

open class GitHubUsernameDataSource(val rxSharedPreferences: RxSharedPreferences,
                                    val sharedPreferences: SharedPreferences): DataSource<String, GitHubUsernameDataSource.GitHubUsernameFetchDataRequirements, String> {

    override fun fetchNewData(requirements: GitHubUsernameFetchDataRequirements): Completable = Completable.complete()

    override fun lastTimeNewDataFetched(): Date? = null

    @SuppressLint("ApplySharedPref")
    override fun saveData(data: String): Completable {
        return Completable.fromCallable {
            sharedPreferences.edit().putString(SharedPrefersKeys.gitHubUsernameKey, data).commit()
        }
    }

    override fun getData(): Observable<String> {
        return rxSharedPreferences.getString(SharedPrefersKeys.gitHubUsernameKey).asObservable()
    }

    override fun cleanup() {
    }

    class GitHubUsernameFetchDataRequirements

}