package com.kylix.model.user

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("password")
    val password: String,
)
