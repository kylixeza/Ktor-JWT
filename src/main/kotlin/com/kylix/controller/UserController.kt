package com.kylix.controller

import com.kylix.model.user.User

interface UserController {
    suspend fun getUserByUsername(username: String): User?

    suspend fun getUserById(uid: String): User?
    suspend fun insertUser(user: User)
}