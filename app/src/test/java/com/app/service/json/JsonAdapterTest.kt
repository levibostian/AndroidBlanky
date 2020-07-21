package com.app.service.json

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.app.service.vo.response.MessageResponseVo
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFails

@RunWith(AndroidJUnit4::class)
class JsonAdapterTest : UnitTest() {

    // fromJson

    @Test
    fun `fromJson - given object string, expect get object in return`() {
        val given =
            """
            {"message": "Message here"}
            """.trimIndent()

        val actual = JsonAdapter.fromJson<MessageResponseVo>(given)

        assertThat(actual).isEqualTo(MessageResponseVo("Message here"))
    }

    @Test
    fun `fromJson - given list of objects string, expect throw`() {
        val given: String = JsonAdapter.toJson(listOf(MessageResponseVo("")))

        assertFails {
            JsonAdapter.fromJson(given)
        }
    }

    @Test
    fun `fromJsonList - given list string, expect get list of objects`() {
        val given =
            """
            [{"message": "Message here"}]
            """.trimIndent()

        val actual: List<MessageResponseVo> = JsonAdapter.fromJsonList(given)

        assertThat(actual).isEqualTo(listOf(MessageResponseVo("Message here")))
    }

    @Test
    fun `fromJsonList - given object string, expect throw`() {
        val given: String = JsonAdapter.toJson(MessageResponseVo(""))

        assertFails {
            JsonAdapter.fromJson(given)
        }
    }

    // toJson

    @Test
    fun `toJson - given object, expect get object string in return`() {
        val expected =
            """
            {"message":"Message here"}
            """.trimIndent()

        val actual = JsonAdapter.toJson(MessageResponseVo("Message here"))

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `toJson - given list of objects, expect get list of object string in return`() {
        val expected =
            """
            [{"message":"Message here"}]
            """.trimIndent()

        val actual = JsonAdapter.toJson(listOf(MessageResponseVo("Message here")))

        assertThat(actual).isEqualTo(expected)
    }
}
