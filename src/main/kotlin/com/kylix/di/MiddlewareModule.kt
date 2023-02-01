package com.kylix.di

import com.kylix.middleware.Middleware
import com.kylix.security.hashing.HashingService
import com.kylix.security.hashing.SHA256HashingService
import com.kylix.security.token.JWTTokenService
import com.kylix.security.token.TokenService
import org.koin.dsl.module

val securityModule = module {
    single<TokenService> { JWTTokenService() }
    single<HashingService> { SHA256HashingService() }
}

val middlewareModule = module {
    single {
        Middleware(
            get(),
            get(),
            get(),
        )
    }
}