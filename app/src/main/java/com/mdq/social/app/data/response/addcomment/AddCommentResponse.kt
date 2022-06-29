package com.mdq.social.app.data.response.addcomment

import com.google.gson.annotations.SerializedName

data class AddCommentResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null

)

data class Data(

	@field:SerializedName("comments")
	val comments: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("sub_comments_id")
	val subCommentsId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("album_id")
	val albumId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null

)
