package com.app.service.http

import com.app.service.util.HostnameProvider
import com.app.service.vo.PokemonVo
import okhttp3.OkHttpClient
import org.json.JSONObject

class PokemonHttpClient(hostnameProvider: HostnameProvider, client: OkHttpClient) : BaseHttpClient(hostnameProvider.pokemonApiHostname, client) {

    suspend fun getPokemonDetails(pokemonName: String): Result<PokemonVo> {
        return get("pokemon/$pokemonName").map { PokemonVo.from(JSONObject(it)) }
    }
}
