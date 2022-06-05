package com.app.di

import com.app.viewmodel.PokemonViewModel

val DiGraph.viewModel: PokemonViewModel
    get() = override() ?: PokemonViewModel(repo)
