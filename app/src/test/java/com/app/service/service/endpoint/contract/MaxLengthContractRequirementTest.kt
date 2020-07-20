package com.app.service.service.endpoint.contract

import com.app.util.RandomStringUtil
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MaxLengthContractRequirementTest {

    private lateinit var contract: MaxLengthContractRequirement

    private val maxLengthRequired = 10

    @Before
    fun setUp() {
        contract = MaxLengthContractRequirement(maxLengthRequired)
    }

    @Test
    fun `assert() - given text too long - expect error`() {
        val differenceInLength = 1
        val text = RandomStringUtil.random(maxLengthRequired + differenceInLength)

        val errorDetailsCaptor = argumentCaptor<MinLengthContractRequirement.ErrorDetails>()
        assertThat(contract.assert(text)).isEqualTo(errorDetailsCaptor.capture())
        assertThat(errorDetailsCaptor.firstValue).isEqualTo(differenceInLength)
    }

    @Test
    fun `assert() - given max length met text - expect null`() {
        val text = RandomStringUtil.random(maxLengthRequired)

        assertThat(contract.assert(text)).isNull()
    }
}
