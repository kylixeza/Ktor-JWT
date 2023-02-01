package com.kylix.plugins

import com.kylix.di.*
import io.ktor.server.application.*
import org.koin.core.logger.Level
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureInjection() {
    install(Koin) {
        slf4jLogger(Level.ERROR)
        modules(
            listOf(
                databaseModule,
                controllerModule,
                securityModule,
                middlewareModule,
                routeModule
            )
        )
    }
}