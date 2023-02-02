package com.kylix.route

import com.kylix.controller.user.UserController
import com.kylix.middleware.Middleware
import com.kylix.model.token.TokenRequest
import com.kylix.model.token.TokenResponse
import com.kylix.model.user.User
import com.kylix.model.user.UserRequest
import com.kylix.route.RouteResponseHelper.buildErrorJson
import com.kylix.route.RouteResponseHelper.buildSuccessJson
import com.kylix.security.hashing.SaltedHash
import com.kylix.security.token.TokenClaim
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.*

class AuthRoute(
    private val userController: UserController,
    private val middleware: Middleware
) {

    private fun Route.signUp() {
        post("signup") {
            val user = try {
                call.receive<UserRequest>()
            } catch (e: Exception) {
                call buildErrorJson e
                return@post
            }

            val saltedHash = middleware.hashPassword(user.password)

            val isUsernameExist = userController.isUsernameExist(user.username)
            if (isUsernameExist) {
                call.buildErrorJson(message = "username already exist, please use another username")
                return@post
            }

            userController.insertUser(
                User(
                    uid = UUID.randomUUID().toString(),
                    username = user.username,
                    password = saltedHash.hash,
                    salt = saltedHash.salt,
                )
            )
            call.buildSuccessJson { "User created" }
        }
    }

    private fun Route.signIn() {
        post("signin") {
            val user = try {
                call.receive<UserRequest>()
            } catch (e: Exception) {
                call buildErrorJson e
                return@post
            }

            val userFromDb = userController.getUserByUsername(user.username)
            if (userFromDb == null) {
                call.buildErrorJson(message = "username not found, please check your username")
                return@post
            }

            middleware.apply { call.verifyPassword(user.password, SaltedHash(
                hash = userFromDb.password,
                salt = userFromDb.salt
            )) }

            val token = middleware.run {
                application.generateToken(
                    TokenClaim("uid", userFromDb.uid),
                )
            }
            call.buildSuccessJson { TokenResponse(token) }
        }
    }

    private fun Route.signOut() {
        post("signout") {
            val jwt = call.request.header("Authorization")?.substring("Bearer ".length)
            middleware.apply {
                application.invalidateToken(jwt ?: "")
            }
            call.buildSuccessJson { "Sign out success" }
        }
    }

    private fun Route.getDetailUser() {
        authenticate {
            get("user") {
                middleware.apply { call.validateToken() }

                val uid = middleware.getClaim<String>(call, "uid")

                val user = kotlin.run {
                    if (uid == null) {
                        call.buildErrorJson(message = "uid not found")
                    } else {
                        userController.getUserById(uid)
                    }
                }

                call.buildSuccessJson { user }
            }
        }
    }

    fun Route.initRoute() {
        this.apply {
            signUp()
            signIn()
            signOut()
            getDetailUser()
        }
    }

}