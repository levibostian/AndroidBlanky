package com.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.service.repository.PokemonRepository
import com.app.service.vo.PokemonVo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PokemonViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) : ViewModel() {

    fun getPokemon(name: String, callback: (result: Result<PokemonVo>) -> Unit) {
        viewModelScope.launch {
            callback(pokemonRepository.getPokemon(name))
        }
    }
}
