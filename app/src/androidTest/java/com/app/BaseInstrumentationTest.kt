package com.app

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.rules.RuleChain
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Base class for running Android instrumentation tests.
 *
 * This class does the following for you:
 * 1. Creates test rules that are very common to use.
 * 2. Makes some convenient assumptions to make running tests quick/easy. Example: If you want to test a Fragment, you only need to override 1 function and the rest is done for you. But this class is also expandable and flexible so you're not locked into doing things certain ways. Feel free to override functions below to make it your own.
 */
abstract class BaseInstrumentationTest {

    abstract fun provideTestClass(): Any

    protected val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    protected val context = instrumentation.targetContext
    protected val application: HiltTestApplication = instrumentation.targetContext.applicationContext as HiltTestApplication

    protected val diRule = HiltAndroidRule(provideTestClass())

    /**
     * Hilt requires that test rules run in a specific order. https://developer.android.com/training/dependency-injection/hilt-testing#multiple-testrules
     * A JUnit [RuleChain] allows that to happen. Here, we have created a chain with the hilt rule already added to it. That means the Hilt rule will run first. You can add more rules to this chain in your test class.
     *
     * Use like:
     * ```
     * @get:Rule var rules = instrumentationTestRules
     * ```
     *
     * If you need to add a rule in your test class, just call [RuleChain.around] on the [instrumentationTestRules] instance in your test class.
     * ```
     * @get:Rule var rules = instrumentationTestRules.around(rule)
     * ```
     */
    protected val instrumentationTestRules: RuleChain = RuleChain
        .outerRule(diRule)

    /**
     * How to create mocks in test class with Mockito.
     *
     * This is a MethodClass for junit, not TestClass so doesn't need to be in [RuleChain].
     *
     * Use:
     * ```
     * @Mock lateinit var foo: Foo // create your mocks in your test class. They will be populated because of this test rule.
     * @get:Rule val mockito = mockitoTestRule
     * ```
     */
    protected val mockitoTestRule: MockitoRule = MockitoJUnit.rule()
}
