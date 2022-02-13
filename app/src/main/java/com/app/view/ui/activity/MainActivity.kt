package com.app.view.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.service.logger.Logger
import com.app.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private val pokemonViewModel: PokemonViewModel by viewModels()
    @Inject lateinit var logger: Logger

    override fun onStart() {
        super.onStart()

        pokemonViewModel.getPokemon("ditto") { apiResult ->
            logger.debug(apiResult.getOrThrow().sprites.frontDefault.toString())
        }
    }
}
