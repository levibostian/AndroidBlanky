package com.app.service.vo

import org.json.JSONObject

data class PokemonVo(
    val name: String,
    val sprites: PokemonSpritesVo
) {
    companion object {
        fun from(json: JSONObject): PokemonVo {
            return PokemonVo(
                name = json.getString("name"),
                sprites = PokemonSpritesVo.from(json.getJSONObject("sprites"))
            )
        }
    }
}
