package com.kylix.data.controller.user

import com.kylix.model.user.User
import com.kylix.model.user.UserResponse

interface UserController {
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserById(uid: String): UserResponse?
    suspend fun insertUser(user: User)
    suspend fun isUsernameExist(username: String): Boolean
}