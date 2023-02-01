package com.kylix.security.token

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    fun invalidate(
        config: TokenConfig,
        token: String
    )
}