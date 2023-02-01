package com.kylix.middleware

import com.kylix.route.RouteResponseHelper.buildErrorJson
import com.kylix.security.hashing.HashingService
import com.kylix.security.hashing.SaltedHash
import com.kylix.security.token.TokenClaim
import com.kylix.security.token.TokenService
import com.kylix.util.Config.tokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.apache.commons.codec.digest.DigestUtils

class Middleware(
    private val tokenService: TokenService,
    private val hashingService: HashingService
) {

    fun hashPassword(password: String) = hashingService.generateSaltedHash(password)

    suspend fun ApplicationCall.verifyPassword(password: String, salt: SaltedHash) {
        val isPasswordValid = hashingService.verify(password, salt)
        if (!isPasswordValid) {
            println("Entered hash: ${DigestUtils.sha256Hex("${salt.salt}${password}")}, Hashed PW: ${salt.hash}")
            buildErrorJson(message = "Invalid password")
        }
    }

    fun Application.generateToken(vararg claims: TokenClaim) = tokenService.generate(
        config = tokenConfig,
        claims = claims
    )

    fun Application.invalidateToken(token: String) = tokenService.invalidate(tokenConfig, token)

    inline fun<reified T: Any> getClaim(call: ApplicationCall, claimName: String) = kotlin.run {
        val principal = call.principal<JWTPrincipal>()
        principal?.getClaim(claimName, T::class)
    }

}