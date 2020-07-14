package com.levibostian.service.service.endpoint.contract

import com.google.common.truth.Truth.assertThat
import com.levibostian.util.RandomStringUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GitHubUsernameContractTest {

    private lateinit var contract: GitHubUsernameContract

    @Test
    fun `verify() - empty username - expect too short`() {
        val username = ""
        contract = GitHubUsernameContract(username)

        assertThat(contract.verify()).isInstanceOf(GitHubUsernameContract.ContractNotMet.TooShort::class.java)
    }

    @Test
    fun `verify() - long username - expect too long`() {
        val username = RandomStringUtil.random(200)
        contract = GitHubUsernameContract(username)

        assertThat(contract.verify()).isInstanceOf(GitHubUsernameContract.ContractNotMet.TooLong::class.java)
    }
}
