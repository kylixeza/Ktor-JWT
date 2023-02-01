package com.kylix.security.token

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

    suspend fun invalidate(
        token: String,
        saveToDb: suspend String.() -> Unit
    )
}