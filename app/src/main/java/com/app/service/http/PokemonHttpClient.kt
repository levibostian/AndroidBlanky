package com.app.service.http

import com.app.service.vo.PokemonVo
import okhttp3.OkHttpClient
import org.json.JSONObject
import javax.inject.Inject

class PokemonHttpClient(hostname: String, client: OkHttpClient) : BaseHttpClient(hostname, client) {

    suspend fun getPokemonDetails(pokemonName: String): Result<PokemonVo> {
        return get("pokemon/$pokemonName").map { PokemonVo.from(JSONObject(it)) }
    }

}
