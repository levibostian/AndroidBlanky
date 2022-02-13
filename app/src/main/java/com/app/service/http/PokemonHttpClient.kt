package com.app.service.http

import okhttp3.OkHttpClient
import javax.inject.Inject

class PokemonHttpClient(hostname: String, client: OkHttpClient) : BaseHttpClient(hostname, client) {

    fun getPokemonDetails(pokemonName: String): Result<String> {
        return get("pokemon/$pokemonName")
    }

}
