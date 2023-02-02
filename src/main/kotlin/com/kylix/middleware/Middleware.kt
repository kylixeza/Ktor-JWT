package com.kylix.middleware

import com.kylix.controller.token.TokenController
import com.kylix.model.token.TokenResponse
import com.kylix.route.RouteResponseHelper.buildErrorJson
import com.kylix.route.RouteResponseHelper.buildSuccessJson
import com.kylix.security.hashing.HashingService
import com.kylix.security.hashing.SaltedHash
import com.kylix.security.token.TokenClaim
import com.kylix.security.token.TokenService
import com.kylix.util.Config.tokenConfig
import io.ktor.client.engine.*
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.*

class Middleware(
    private val tokenController: TokenController,
    private val tokenService: TokenService,
    private val hashingService: HashingService
) {

    fun hashPassword(password: String) = hashingService.generateSaltedHash(password)

    suspend fun ApplicationCall.verifyPassword(password: String, salt: SaltedHash) {
        val isPasswordValid = hashingService.verify(password, salt)
        if (!isPasswordValid) {
            buildErrorJson(message = "Invalid password")
        }
    }

    fun Application.generateToken(vararg claims: TokenClaim) = tokenService.generate(
        config = tokenConfig,
        claims = claims
    )

    suspend fun Application.invalidateToken(token: String) {
        tokenService.apply {
            invalidate(token) { tokenController.insertToBlacklist(this) }
        }
    }

    suspend fun ApplicationCall.validateToken() {
        val jwt = request.header("Authorization")?.substring("Bearer ".length)
        val isValid = tokenController.isTokenValid(jwt)
        if (!isValid) {
            buildErrorJson(httpStatusCode = Unauthorized, message = "Invalid token")
        }
    }

    inline fun<reified T: Any> getClaim(call: ApplicationCall, claimName: String) = kotlin.run {
        val principal = call.principal<JWTPrincipal>()
        principal?.getClaim(claimName, T::class)
    }

}