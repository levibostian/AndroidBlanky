package com.app.service.repository
import com.app.service.DispatcherProvider
import com.app.service.http.PokemonHttpClient
import com.app.service.vo.PokemonVo
import javax.inject.Inject
import kotlinx.coroutines.withContext

class PokemonRepository @Inject constructor(
    private val http: PokemonHttpClient,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun getPokemon(name: String): Result<PokemonVo> = withContext(dispatcherProvider.io()) {
        http.getPokemonDetails(name)
    }
}
