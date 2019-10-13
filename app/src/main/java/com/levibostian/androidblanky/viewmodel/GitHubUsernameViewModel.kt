package com.levibostian.androidblanky.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.extensions.toLiveData
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.service.endpoint.GetGitHubReposEndpoint
import com.levibostian.androidblanky.service.service.endpoint.contract.GitHubUsernameContract
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.teller.cachestate.LocalCacheState
import java.util.*

@OpenForTesting
class GitHubUsernameViewModel(private val context: Context,
                              private val repository: GitHubUsernameRepository): ViewModel() {

    private val repositoryRequirements = GitHubUsernameRepository.Requirements()

    fun setUsername(username: String) {
        repository.newCache(username, repositoryRequirements)
    }

    fun observeUsername(): LiveData<LocalCacheState<String>> {
        repository.requirements = repositoryRequirements
        return repository.observe().toLiveData()
    }

    fun validateUsername(username: String): String? {
        val usernameContract = GetGitHubReposEndpoint.githubUsernameContract(username)

        usernameContract.verify()?.let { contractNotMetDetails ->
            return when (contractNotMetDetails) {
                is GitHubUsernameContract.ContractNotMet.TooLong -> context.getString(R.string.username_too_long).format(Locale.getDefault(), contractNotMetDetails.details.difference)
                is GitHubUsernameContract.ContractNotMet.TooShort -> context.getString(R.string.username_too_short).format(Locale.getDefault(), contractNotMetDetails.details.difference)
            }
        }

        return null
    }

}