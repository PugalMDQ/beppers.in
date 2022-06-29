package com.mdq.social.app.data.response.service

import com.google.gson.annotations.SerializedName

data class ServiceResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("service")
	val service: String? = null,

	@field:SerializedName("service_id")
	val serviceId: Int? = null
)
