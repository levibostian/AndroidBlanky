package com.levibostian.androidblanky.service.datasource

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import java.util.*

open class GitHubUsernameDataSource(private val rxSharedPreferencesWrapper: RxSharedPreferencesWrapper,
                                    override val sharedPreferences: SharedPreferences): DataSource<String, GitHubUsernameDataSource.GitHubUsernameFetchDataRequirements, String>(sharedPreferences) {

    override fun lastTimeNewDataFetchedKey(): String = throw RuntimeException("You should never call this. This data source never needs updated.")

    @SuppressLint("ApplySharedPref")
    override fun deleteData(): Completable = Completable.fromCallable { sharedPreferences.edit().putString(SharedPrefersKeys.gitHubUsernameKey, "").commit() }

    override fun fetchNewData(requirements: GitHubUsernameFetchDataRequirements): Completable = Completable.complete()

    @SuppressLint("ApplySharedPref")
    override fun saveData(data: String): Completable {
        return Completable.fromCallable {
            sharedPreferences.edit().putString(SharedPrefersKeys.gitHubUsernameKey, data).commit()
        }
    }

    override fun getData(): Observable<String> {
        return rxSharedPreferencesWrapper.getString(SharedPrefersKeys.gitHubUsernameKey).asObservable()
    }

    override fun cleanup() {
    }

    class GitHubUsernameFetchDataRequirements

}