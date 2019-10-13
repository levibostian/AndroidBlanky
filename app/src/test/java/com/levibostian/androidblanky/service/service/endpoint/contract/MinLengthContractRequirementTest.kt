package com.levibostian.androidblanky.service.service.endpoint.contract

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.levibostian.androidblanky.util.RandomStringUtil
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlin.math.min

@RunWith(MockitoJUnitRunner::class)
class MinLengthContractRequirementTest {

    private lateinit var contract: MinLengthContractRequirement

    private val minLengthRequired = 3

    @Before
    fun setUp() {
        contract = MinLengthContractRequirement(minLengthRequired)
    }

    @Test
    fun `assert() - given text too short - expect error`() {
        val differenceInLength = 1
        val text = RandomStringUtil.random(minLengthRequired - differenceInLength)

        val errorDetailsCaptor = argumentCaptor<MinLengthContractRequirement.ErrorDetails>()
        assertThat(contract.assert(text)).isEqualTo(errorDetailsCaptor.capture())
        assertThat(errorDetailsCaptor.firstValue).isEqualTo(differenceInLength)
    }

    @Test
    fun `assert() - given blank text too short - expect error`() {
        val errorDetailsCaptor = argumentCaptor<MinLengthContractRequirement.ErrorDetails>()
        assertThat(contract.assert("")).isEqualTo(errorDetailsCaptor.capture())
        assertThat(errorDetailsCaptor.firstValue).isEqualTo(minLengthRequired)
    }

    @Test
    fun `assert() - given min length met text - expect null`() {
        val text = RandomStringUtil.random(minLengthRequired)

        assertThat(contract.assert(text)).isNull()
    }

}