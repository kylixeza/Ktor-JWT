package com.kylix.util

import com.kylix.security.token.TokenConfig
import io.ktor.server.application.*

object Config {

    val Application.tokenConfig: TokenConfig
        get() = TokenConfig(
            issuer = environment.config.property("jwt.issuer").getString(),
            audience = environment.config.property("jwt.audience").getString(),
            expiresIn = 365L * 1000L * 60L * 60L * 24L,
            secret = System.getenv("JWT_SECRET")
        )

}