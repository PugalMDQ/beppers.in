package com.mdq.social.app.data.response.commentdetails

import com.google.gson.annotations.SerializedName

data class CommentDetailsResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("total_comments")
	val totalComments: Int? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("comments")
	val comments: List<CommentsItem?>? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("total_views")
	val totalViews: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("total_likes")
	val totalLikes: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("gallery_id")
	val galleryId: Int? = null,

	@field:SerializedName("total_subscribe")
	val totalSubscribe: String? = null
)

data class CommentsItem(

	@field:SerializedName("comments")
	val comments: String? = null,

	@field:SerializedName("subcomments")
	val subcomments: List<SubcommentsItem?>? = null,

	@field:SerializedName("sub_comments_id")
	val subCommentsId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("comments_id")
	val commentsId: Int? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("time")
	val time: String? = null
)

data class SubcommentsItem(

	@field:SerializedName("comments")
	val comments: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("sub_comments_id")
	val subCommentsId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("comments_id")
	val commentsId: Int? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
