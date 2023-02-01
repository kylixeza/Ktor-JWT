package com.kylix.model

import com.google.gson.annotations.SerializedName


data class BaseListResponse<T>(
	@field:SerializedName("status")
	var status: String = "",
	@field:SerializedName("message")
	val message: String = "",
	@field:SerializedName("count")
	val count: Int = 0,
	@field:SerializedName("data")
	val data: T?

)
