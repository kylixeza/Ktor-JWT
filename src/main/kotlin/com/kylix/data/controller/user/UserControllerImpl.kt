package com.kylix.data.controller.user

import com.kylix.data.DatabaseFactory
import com.kylix.model.user.User
import com.kylix.model.user.UserResponse
import com.kylix.data.table.UserTable
import com.kylix.util.toUser
import com.kylix.util.toUserResponse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserControllerImpl(
    private val dbFactory: DatabaseFactory
): UserController {
    override suspend fun getUserByUsername(username: String): User? = dbFactory.dbQuery {
        UserTable.select {
            UserTable.username eq username
        }.map {
            it.toUser()
        }.firstOrNull()
    }

    override suspend fun getUserById(uid: String): UserResponse? = dbFactory.dbQuery {
        UserTable.select {
            UserTable.uid eq uid
        }.map {
            it.toUserResponse()
        }.firstOrNull()
    }

    override suspend fun insertUser(user: User): Unit = dbFactory.dbQuery {
        UserTable.insert {
            it[uid] = user.uid
            it[username] = user.username
            it[password] = user.password
            it[salt] = user.salt
        }
    }

    override suspend fun isUsernameExist(username: String): Boolean = dbFactory.dbQuery {
        UserTable.select {
            UserTable.username eq username
        }.count() > 0
    }

}