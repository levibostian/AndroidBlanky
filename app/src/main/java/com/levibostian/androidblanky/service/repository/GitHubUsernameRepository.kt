package com.levibostian.androidblanky.service.repository

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.levibostian.teller.repository.LocalRepository
import io.reactivex.Completable
import io.reactivex.Observable
import android.content.SharedPreferences
import androidx.core.content.edit
import com.levibostian.androidblanky.extensions.observeString
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class GitHubUsernameRepository(private val sharedPreferences: SharedPreferences): LocalRepository<String, GitHubUsernameRepository.Requirements>() {

    private val githubUsernameSharedPrefsKey = "repository_githubUsername_key"

    override fun saveCache(cache: String, requirements: Requirements) {
        sharedPreferences.edit {
            putString(githubUsernameSharedPrefsKey, cache)
        }
    }

    override fun observeCache(requirements: Requirements): Observable<String> {
        return sharedPreferences.observeString(githubUsernameSharedPrefsKey)
                .subscribeOn(Schedulers.io())
    }

    override fun isCacheEmpty(cache: String, requirements: Requirements): Boolean = cache.isBlank()

    class Requirements: LocalRepository.GetCacheRequirements

}