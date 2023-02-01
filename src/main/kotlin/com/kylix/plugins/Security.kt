package com.kylix.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kylix.util.Config.tokenConfig
import io.ktor.server.application.*

fun Application.configureSecurity() {

    authentication {
            jwt {
                val tokenConfig = this@configureSecurity.tokenConfig
                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(tokenConfig.secret))
                        .withAudience(tokenConfig.audience)
                        .withIssuer(tokenConfig.issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(tokenConfig.audience)) {
                        JWTPrincipal(credential.payload)
                    } else null
                }
            }
        }
}
