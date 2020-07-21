package com.app.service.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StringReplaceUtilTest : UnitTest() {

    @Test
    fun test_replace_givenAllTemplates_expectTemplateReplaced() {
        StringReplaceTemplate.values().forEach { template ->
            val originalString = template.pattern
            val actual = StringReplaceUtil.replace(template.pattern)

            // If they are the same, the template did not get replaced
            assertThat(originalString).isNotEqualTo(actual)
        }
    }

    @Test
    fun test_replace_givenValuesToReplaceWith_expectReplacementSuccessful() {
        val given = "Workouts for week {{week}}"
        val expected = "Workouts for week 10"

        val actual = StringReplaceUtil.replace(
            given,
            listOf(
                Pair("week", 10.toString())
            )
        )

        assertThat(expected).isEqualTo(actual)
    }
}
