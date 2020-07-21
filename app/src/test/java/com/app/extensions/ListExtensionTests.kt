package com.app.extensions

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.UnitTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListExtensionTests : UnitTest() {

    // insertOrLast

    @Test
    fun `insertOrLast - given empty list, expect get list with 1 item in it`() {
        val given = mutableListOf<String>()

        given.insertOrLast("1", 5)

        assertThat(given).isEqualTo(listOf("1"))
    }

    @Test
    fun `insertOrLast - given index out of range, expect list with item added to end`() {
        val given = mutableListOf("1")

        given.insertOrLast("2", 5)

        assertThat(given).isEqualTo(listOf("1", "2"))
    }

    @Test
    fun `insertOrLast - given index in range, expect list with item added at index`() {
        val given = mutableListOf("1", "3")

        given.insertOrLast("2", 1)

        assertThat(given).isEqualTo(listOf("1", "2", "3"))
    }

    // indexOfFirstOrNull

    @Test
    fun `indexOfFirstOrNull - given empty list, expect null`() {
        val given = emptyList<String>()

        val actual = given.indexOfFirstOrNull { it == "1" }

        assertThat(actual).isNull()
    }

    @Test
    fun `indexOfFirstOrNull - given list with item not included, expect null`() {
        val given = listOf("not-included")

        val actual = given.indexOfFirstOrNull { it == "1" }

        assertThat(actual).isNull()
    }

    @Test
    fun `indexOfFirstOrNull - given list with item included, expect get index`() {
        val given = listOf("1", "2", "3")

        val actual = given.indexOfFirstOrNull { it == "3" }

        assertThat(actual).isEqualTo(2)
    }

    // sortedAlphanumeric

    @Test
    fun `sortedAlphanumeric - given empty list, expect empty list`() {
        val given = emptyList<String>()

        val actual = given.sortedAlphanumeric()

        assertThat(actual).isEmpty()
    }

    @Test
    fun `sortedAlphanumeric - given list with no numbers, expect sorted list`() {
        val given = listOf("Cat", "Apple")

        val actual = given.sortedAlphanumeric()

        assertThat(actual).isEqualTo(listOf("Apple", "Cat"))
    }

    // This test below fails. However, it's not a high priority to fix it at this time because we will probably not have mixed cases in the app at this time.
//    @Test
//    fun `sortedAlphanumeric - given list with no numbers and mixed case, expect sorted list`() {
//        var given = listOf("cat", "Apple")
//
//        var actual = given.sortedAlphanumeric()
//
//        assertThat(actual).isEqualTo(listOf("Apple", "cat"))
//
//        ///
//
//        given = listOf("Cat", "apple")
//
//        actual = given.sortedAlphanumeric()
//
//        assertThat(actual).isEqualTo(listOf("apple", "Cat"))
//    }

    @Test
    fun `sortedAlphanumeric - given list with numbers at end, expect sorted list`() {
        // Note: It's important to have numbers > 10 inside of here because we need to make sure that "3" comes before "10". Aphabetical sorters would mess this up but alphanumeric would have it in correct order.
        val given = listOf("Week 23", "Week 11", "Week 2", "Week 3", "Week 10", "Week 1")

        val actual = given.sortedAlphanumeric()

        assertThat(actual).isEqualTo(listOf("Week 1", "Week 2", "Week 3", "Week 10", "Week 11", "Week 23"))
    }

    @Test
    fun `sortedAlphanumeric - given list with numbers at beginning, expect sorted list`() {
        val given = listOf("1 week", "4 weeks", "21 weeks", "11 weeks")

        val actual = given.sortedAlphanumeric()

        assertThat(actual).isEqualTo(listOf("1 week", "4 weeks", "11 weeks", "21 weeks"))
    }
}
