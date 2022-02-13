package com.app

import android.content.SharedPreferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.service.store.KeyValueStorageKey
import com.app.service.http.PokemonHttpClient
import com.app.service.store.KeyValueStorage
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleSharedTest : BaseTest() {

    override fun provideTestClass(): Any = this
    @get:Rule val runRules = testRules

    @Inject lateinit var pokemonHttpClient: PokemonHttpClient
    @Inject lateinit var sharedPrefs: SharedPreferences

    @Test
    fun test_sharedPreferencesWorks() {
        val keyValueStorage = KeyValueStorage(sharedPrefs)

        keyValueStorage.setString("foo", KeyValueStorageKey.LOGGED_IN_USER_AUTH_TOKEN)

        assertThat(keyValueStorage.string(KeyValueStorageKey.LOGGED_IN_USER_AUTH_TOKEN)).isEqualTo("foo")
    }

    @Test
    fun test_mockWebServerWorks() {
        val expectedBody = "{\"foo\": \"bar\"}"
        mockWebServer.queue(200, expectedBody)

        val response = pokemonHttpClient.getPokemonDetails("foo")
        val actualBody = response.getOrThrow()

        assertThat(actualBody).isEqualTo(expectedBody)
    }

}
