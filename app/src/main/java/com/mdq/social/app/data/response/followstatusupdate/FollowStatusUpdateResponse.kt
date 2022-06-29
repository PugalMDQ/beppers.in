package com.mdq.social.app.data.response.followstatusupdate

import com.google.gson.annotations.SerializedName

data class FollowStatusUpdateResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
