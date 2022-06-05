package com.app.di

import com.app.service.DispatcherProvider
import com.app.service.ImplementationDispatcherProvider
import com.app.service.logger.Logger

val DiGraph.dispatcherProvider: DispatcherProvider
    get() = override() ?: ImplementationDispatcherProvider()

val DiGraph.logger: Logger
    get() = override() ?: Logger(contextProvider)
