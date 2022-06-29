package com.mdq.social.app.data.response.livechat

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LiveChatResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null
):Serializable

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("from_id")
	val from_id: Int? = null,

	@field:SerializedName("to_id")
	val to_id: Int? = null,

	@field:SerializedName("messages")
	val messages: String? = null,

	@field:SerializedName("created_time")
	val created_time: String? = null,

	@field:SerializedName("user_name")
	val user_name: String? = null,

	@field:SerializedName("profile_photo")
	val profile_photo: String? = null
):Serializable
