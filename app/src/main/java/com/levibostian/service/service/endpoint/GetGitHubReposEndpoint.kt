package com.levibostian.service.service.endpoint

import com.levibostian.service.service.endpoint.contract.Contract
import com.levibostian.service.service.endpoint.contract.GitHubUsernameContract
import com.levibostian.service.service.endpoint.contract.MaxLengthContractRequirement

object GetGitHubReposEndpoint: Endpoint {

    fun githubUsernameContract(username: String): GitHubUsernameContract = GitHubUsernameContract(username)

}