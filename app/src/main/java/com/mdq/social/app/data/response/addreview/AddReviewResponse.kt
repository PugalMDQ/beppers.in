package com.mdq.social.app.data.response.addreview

import com.google.gson.annotations.SerializedName

data class AddReviewResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
