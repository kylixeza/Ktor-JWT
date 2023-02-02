package com.kylix.data.controller.token

interface TokenController {

    suspend fun insertToBlacklist(token: String)

    suspend fun isTokenValid(token: String?): Boolean

}