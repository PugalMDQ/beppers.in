package com.mdq.social.app.data.response.useralbum

import com.google.gson.annotations.SerializedName

data class UserAlbumResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null
)

data class RecentItem(

	@field:SerializedName("total_comments")
	val totalComments: Int? = null,

	@field:SerializedName("total_like")
	val totalLike: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("comments")
	val comments: String? = null,

	@field:SerializedName("like")
	val like: Int? = null,

	@field:SerializedName("subscribe")
	val subscribe: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("total_view")
	val totalView: String? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("gallery_id")
	val galleryId: Int? = null,

	@field:SerializedName("commentscnt")
	val commentscnt: Int? = null,

	@field:SerializedName("total_subscribe")
	val totalSubscribe: String? = null
)

data class RecommendedItem(

	@field:SerializedName("total_comments")
	val totalComments: Int? = null,

	@field:SerializedName("total_like")
	var totalLike: Int? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("comments")
	val comments: String? = null,

	@field:SerializedName("like")
	var like: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("total_view")
	val totalView: String? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("gallery_id")
	val galleryId: Int? = null,

	@field:SerializedName("total_subscribe")
	val totalSubscribe: String? = null
)

data class TrendingItem(

	@field:SerializedName("total_comments")
	val totalComments: Int? = null,

	@field:SerializedName("total_like")
	val totalLike: String? = null,

	@field:SerializedName("comments")
	val comments: String? = null,

	@field:SerializedName("like")
	val like: Int? = null,

	@field:SerializedName("subscribe")
	val subscribe: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("total_view")
	val totalView: String? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("gallery_id")
	val galleryId: Int? = null,

	@field:SerializedName("total_subscribe")
	val totalSubscribe: String? = null
)

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("tag")
	val tag: String? = null,

	@field:SerializedName("gallery")
	val gallery: String? = null,

	@field:SerializedName("user_id")
	val user_id: String? = null,

)
