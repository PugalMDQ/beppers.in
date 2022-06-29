package com.mdq.social.app.data.response.usertimes

import com.google.gson.annotations.SerializedName

data class UserTimesResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("shoptime_id")
	val shoptimeId: Int? = null,

	@field:SerializedName("service_id")
	val serviceId: Int? = null,

	@field:SerializedName("evening_time")
	val eveningTime: String? = null,

	@field:SerializedName("day")
	val day: String? = null,

	@field:SerializedName("morning_time")
	val morningTime: String? = null
)
