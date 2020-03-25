package com.levibostian.service.service.endpoint.contract

open class MaxLengthContractRequirement(private val maxLength: Int): TextContractRequirement() {

    override fun assert(text: String): ErrorDetails? {
        if (text.length <= maxLength) return null

        return ErrorDetails(text.length - maxLength)
    }

    data class ErrorDetails(val difference: Int): RequirementErrorDetails()

}