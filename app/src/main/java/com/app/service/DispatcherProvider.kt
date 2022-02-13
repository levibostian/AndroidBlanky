package com.app.service

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}

/**
 * Way to differentiate Dispatchers (IO vs Main, for example). There needs to be a way to differentiate for dependency injection.
 */
class ImplementationDispatcherProvider : DispatcherProvider {

    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun main(): CoroutineDispatcher = Dispatchers.Main
}
