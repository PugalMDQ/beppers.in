package com.mdq.social.app.data.response.updatedLikeUnlike

import com.google.gson.annotations.SerializedName

data class updatedLikeUnlike(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null

)

data class DataItem(

	@field:SerializedName("no_of_likes")
	val no_of_likes: String? = null

)
