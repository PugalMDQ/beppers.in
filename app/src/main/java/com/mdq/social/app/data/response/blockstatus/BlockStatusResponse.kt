package com.mdq.social.app.data.response.blockstatus

import com.google.gson.annotations.SerializedName

data class BlockStatusResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
