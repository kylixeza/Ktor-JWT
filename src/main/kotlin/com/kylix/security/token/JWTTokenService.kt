package com.kylix.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class JWTTokenService: TokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        var token =  JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach {
            token = token.withClaim(it.name, it.value)
        }
        token = token.withClaim("exp", config.expiresIn)

        return token.sign(Algorithm.HMAC256(config.secret))
    }

    override fun invalidate(config: TokenConfig, token: String) {
        JWT.require(Algorithm.HMAC256(config.secret))
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .build()
            .verify(token)
            .getClaim("exp")
            .asDate()
            .time = System.currentTimeMillis()
    }
}