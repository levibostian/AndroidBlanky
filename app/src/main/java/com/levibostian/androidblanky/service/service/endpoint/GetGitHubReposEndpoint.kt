package com.levibostian.androidblanky.service.service.endpoint

import com.levibostian.androidblanky.service.service.endpoint.contract.Contract
import com.levibostian.androidblanky.service.service.endpoint.contract.GitHubUsernameContract
import com.levibostian.androidblanky.service.service.endpoint.contract.MaxLengthContractRequirement

object GetGitHubReposEndpoint: Endpoint {

    fun githubUsernameContract(username: String): GitHubUsernameContract = GitHubUsernameContract(username)

}