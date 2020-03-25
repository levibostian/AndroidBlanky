package com.levibostian.service.repository

import com.levibostian.teller.repository.LocalRepository
import io.reactivex.Observable
import android.content.SharedPreferences
import androidx.core.content.edit
import com.levibostian.extensions.observeString
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GitHubUsernameRepository @Inject constructor(private val sharedPreferences: SharedPreferences): LocalRepository<String, GitHubUsernameRepository.Requirements>() {

    private val githubUsernameSharedPrefsKey = "repository_githubUsername_key"

    public override fun saveCache(cache: String, requirements: Requirements) {
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