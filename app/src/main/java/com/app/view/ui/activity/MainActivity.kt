package com.app.view.ui.activity

import androidx.appcompat.app.AppCompatActivity
import com.app.di.DiGraph
import com.app.di.logger
import com.app.di.viewModel

class MainActivity : AppCompatActivity() {

    private val pokemonViewModel = DiGraph.viewModel
    private val logger = DiGraph.logger

    override fun onStart() {
        super.onStart()

        pokemonViewModel.getPokemon("ditto") { apiResult ->
            logger.debug(apiResult.getOrThrow().sprites.frontDefault.toString())
        }
    }
}
