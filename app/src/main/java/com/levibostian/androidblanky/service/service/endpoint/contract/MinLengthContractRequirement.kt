package com.levibostian.androidblanky.service.service.endpoint.contract

class MinLengthContractRequirement(private val minLength: Int): TextContractRequirement() {

    override fun assert(text: String): ErrorDetails? {
        if (text.length >= minLength) return null

        return ErrorDetails(minLength - text.length)
    }

    data class ErrorDetails(val difference: Int): RequirementErrorDetails()

}