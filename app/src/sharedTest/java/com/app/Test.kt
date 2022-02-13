package com.app

import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry
import com.app.testutils.rule.MainCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltTestApplication
import java.io.BufferedReader
import java.io.InputStreamReader
import org.junit.After
import org.junit.Before
import org.junit.rules.RuleChain

/**
 * Contains code that *all* types of tests (JVM and Android instrumentation) contain.
 *
 * This class is meant to be inherited by another abstract class, not used directly. Because of that, do *not* include @Inject statements in here as they may not work get initialized.
 */
abstract class Test {

    /**
     * In test class, have the line: `override fun provideTestClass(): Any = this` to satisfy this
     */
    abstract fun provideTestClass(): Any

    protected val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    protected val context = instrumentation.targetContext // context of Android app
    protected val testContext = instrumentation.context // context of Test project
    protected val application: HiltTestApplication = instrumentation.targetContext.applicationContext as HiltTestApplication

    /**
     * Allow Hilt to construct objects in your test classes. To do this, in your test class:
     * 1. Add dependencies you wish to inject: `@Inject lateinit var mockWebServer: MockWebServer`
     * 2. Add annotations to the top of your test class:
     * ```
     * @HiltAndroidTest
     * class FooTest: BaseInstrumentationTest() {
     * ```
     * 3. By using the main test rule in your tests, you will be using this Hilt rule:
     * ```
     * @get:Rule val runRules = testRules
     * ```
     */
    protected val diRule = HiltAndroidRule(provideTestClass())

    /**
     * Rule to run Kotlin Coroutines synchronously for predictable tests and allows us to run tests in the JVM (since the Android main thread doesn't exist in JVM tests).
     */
    protected val mainCoroutineRule = MainCoroutineRule()

    /**
     * Main test rule to use in your test classes. These test rules *are* recommended for testing backend/business logic code to run it in a synchronous way.
     *
     * Not recommended to use in Android instrumentation UI test classes as tests fail when you introduce replacing threads. The tests seem to work fine using the app given threads. Espresso tests can use Idling Resources to avoid needing to replace threads and Espresso tests should run as close to possible to production code for testing code accurately.
     *
     * Use like:
     * ```
     * @get:Rule val runRules = testRules
     * ```
     *
     * If you need to add a rule in your test class, just call [RuleChain.around] on the [instrumentationTestRules] instance in your test class.
     * ```
     * @get:Rule var runRules = testRules.around(otherRule)
     * ```
     *
     * Note: Hilt requires that test rules run in a specific order. https://developer.android.com/training/dependency-injection/hilt-testing#multiple-testrules
     * A JUnit [RuleChain] allows that to happen. Here, we have created a chain with the hilt rule already added to it. That means the Hilt rule will run first. You can add more rules to this chain in your test class.
     */
    protected val testRules: RuleChain = RuleChain
        .outerRule(diRule)
        .around(mainCoroutineRule)

    enum class TestAssetFiles {
        GET_POKEMON_SUCCESS {
            override val filename: String = "get_pokemon_success.json"
        };

        abstract val filename: String
    }

    protected fun getAssetContents(asset: TestAssetFiles): String {
        val reader = BufferedReader(InputStreamReader(testContext.assets.open(asset.filename)))

        val sb = StringBuilder()
        var strLine: String?

        while (reader.readLine().also { strLine = it } != null) {
            sb.append(strLine)
        }

        return sb.toString()
    }

    @Before
    open fun setup() {}

    @After
    open fun teardown() {}
}
