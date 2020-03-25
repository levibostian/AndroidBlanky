package com.levibostian.service.service.endpoint.contract

abstract class TextContractRequirement {

    /**
     * Here, we check if requirements are correct for the given data.
     *
     * It is up to you to decide what is correct and not from the given data.
     */
    abstract fun assert(text: String): RequirementErrorDetails?

    /**
     * make a data class filled with information about the error so you can diagnose what is wrong.
     */
    abstract class RequirementErrorDetails

}