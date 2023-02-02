package com.kylix.util

import com.kylix.model.user.User
import com.kylix.model.user.UserResponse
import com.kylix.table.UserTable
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser() = User(
    uid = this[UserTable.uid],
    username = this[UserTable.username],
    password = this[UserTable.password],
    salt = this[UserTable.salt]
)

fun ResultRow.toUserResponse() = UserResponse(
    uid = this[UserTable.uid],
    username = this[UserTable.username]
)