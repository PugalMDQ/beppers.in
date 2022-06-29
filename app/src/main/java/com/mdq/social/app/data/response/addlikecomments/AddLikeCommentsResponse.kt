package com.mdq.social.app.data.response.addlikecomments

import com.google.gson.annotations.SerializedName

data class AddLikeCommentsResponse(


	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null
)


data class DataItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Int? = null,

	@field:SerializedName("comments")
	val totalView: Int? = null,

	@field:SerializedName("subscribe")
	val subscribe: Int? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("user_id")
	val galleryId: Int? = null,

	@field:SerializedName("view")
	val view: Int? = null,

	@field:SerializedName("like")
	val like: Int? = null
)
