package com.kylix.di

import com.kylix.route.AuthRoute
import org.koin.dsl.module

val routeModule = module {
    factory {
        AuthRoute(
            get(),
            get()
        )
    }
}