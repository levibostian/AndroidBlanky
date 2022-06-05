package com.app.di

import com.app.service.repository.PokemonRepository

val DiGraph.repo: PokemonRepository
    get() = override() ?: PokemonRepository(http = httpClient, dispatcherProvider = dispatcherProvider)
