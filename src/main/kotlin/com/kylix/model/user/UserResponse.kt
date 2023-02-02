package com.kylix.model.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("uid")
    val uid: String,

    @field:SerializedName("username")
    val username: String,
)
