package com.mdq.social.app.data.response.blocklist

import com.google.gson.annotations.SerializedName

data class BlockListResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("blocked_user_id")
	val blockedUserId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("create_date")
	val createDate: String? = null,

	@field:SerializedName("blocked_status")
	val blockedStatus: Int? = null
)
