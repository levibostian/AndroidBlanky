package com.levibostian.androidblanky.service.service.endpoint.contract

class GitHubUsernameContract(private val username: String): Contract() {

    private val notTooShort = MinLengthContractRequirement(3)
    private val notTooLong = MaxLengthContractRequirement(30)

    override fun verify(): ContractNotMet? {
        notTooShort.assert(username)?.let { return ContractNotMet.TooShort(it) }
        notTooLong.assert(username)?.let { return ContractNotMet.TooLong(it) }

        return null
    }

    sealed class ContractNotMet: Contract.ContractNotMet() {
        data class TooLong(val details: MaxLengthContractRequirement.ErrorDetails): ContractNotMet()
        data class TooShort(val details: MinLengthContractRequirement.ErrorDetails): ContractNotMet()
    }

}