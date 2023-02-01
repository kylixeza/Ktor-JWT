package com.kylix.model.token

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @field:SerializedName("token")
    val token: String
)