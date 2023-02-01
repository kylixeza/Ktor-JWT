package com.kylix.controller.token

import com.kylix.data.DatabaseFactory
import com.kylix.table.TokenBlacklistTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class TokenControllerImpl(
    private val dbFactory: DatabaseFactory
): TokenController{
    override suspend fun insertToBlacklist(token: String): Unit = dbFactory.dbQuery {
        TokenBlacklistTable.insert {
            it[TokenBlacklistTable.token] = token
        }
    }

    override suspend fun isTokenValid(token: String?): Boolean = dbFactory.dbQuery {
        if (token == null) return@dbQuery false
        TokenBlacklistTable.select {
            TokenBlacklistTable.token eq token
        }.empty()
    }

}