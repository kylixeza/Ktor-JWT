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

        return token.sign(Algorithm.HMAC256(config.secret))
    }

    //invalidate token
    override suspend fun invalidate(token: String, saveToDb: suspend String.() -> Unit) {
        token.saveToDb()
    }
}