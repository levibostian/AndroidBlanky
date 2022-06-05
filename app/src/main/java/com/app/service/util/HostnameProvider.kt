package com.app.service.util

interface HostnameProvider {
    val pokemonApiHostname: String
}

class HostnameProviderImpl : HostnameProvider {
    override val pokemonApiHostname: String = "https://pokeapi.co/api/v2/"
}
