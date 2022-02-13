package com.app.service.vo

import android.net.Uri
import org.json.JSONObject

data class PokemonSpritesVo(
    val frontDefault: Uri,
    val frontShiny: Uri
) {
    companion object {
        fun from(json: JSONObject): PokemonSpritesVo = PokemonSpritesVo(
            frontDefault = Uri.parse(json.getString("front_default")),
            frontShiny = Uri.parse(json.getString("front_shiny"))
        )
    }
}
