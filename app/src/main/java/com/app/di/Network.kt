package com.app.di

import com.app.service.http.PokemonHttpClient
import com.app.service.util.HostnameProvider
import com.app.service.util.HostnameProviderImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

val DiGraph.httpClient: PokemonHttpClient
    get() = override() ?: PokemonHttpClient(hostnameProvider = hostnameProvider, client = okHttpClient)

val DiGraph.hostnameProvider: HostnameProvider
    get() = override() ?: HostnameProviderImpl()

private val DiGraph.okHttpClient: OkHttpClient
    get() = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()
