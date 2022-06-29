package com.mdq.social.app.data.response.times

import com.google.gson.annotations.SerializedName

data class TimesResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("shoptime_id")
	val shoptimeId: Int? = null,

	@field:SerializedName("evening_time")
	val eveningTime: String? = null,

	@field:SerializedName("day")
	val day: String? = null,

	@field:SerializedName("morning_time")
	val morningTime: String? = null
)
