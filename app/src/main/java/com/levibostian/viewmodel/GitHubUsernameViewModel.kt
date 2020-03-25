package com.levibostian.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levibostian.R
import com.levibostian.extensions.toLiveData
import com.levibostian.service.repository.GitHubUsernameRepository
import com.levibostian.service.service.endpoint.GetGitHubReposEndpoint
import com.levibostian.service.service.endpoint.contract.GitHubUsernameContract
import com.levibostian.testing.OpenForTesting
import com.levibostian.teller.cachestate.LocalCacheState
import java.util.*
import javax.inject.Inject

@OpenForTesting
class GitHubUsernameViewModel @Inject constructor(private val context: Context,
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