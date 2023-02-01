package com.kylix.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("uid")
    val uid: String,
    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("password")
    val password: String,
    @field:SerializedName("salt")
    val salt: String,
)
