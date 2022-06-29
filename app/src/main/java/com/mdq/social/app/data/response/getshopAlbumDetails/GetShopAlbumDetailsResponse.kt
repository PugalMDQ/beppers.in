package com.mdq.social.app.data.response.getshopAlbumDetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetShopAlbumDetailsResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
):Serializable

data class VediosItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("total_comments_count")
	val totalCommentsCount: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("total_view_count")
	val totalViewCount: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("media_type")
	val mediaType: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("total_like_count")
	val totalLikeCount: String? = null,

	@field:SerializedName("total_subscribe_count")
	val totalSubscribeCount: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("gallery_id")
	val galleryId: Int? = null,

	@field:SerializedName("bussines_id")
	val bussinesId: Int? = null
):Serializable

data class LocationItem(

	@field:SerializedName("map_latitude_longitude")
	val mapLatitudeLongitude: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("map_link")
	val mapLink: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("bussines_id")
	val bussinesId: Int? = null
):Serializable

data class DataItem(

	@field:SerializedName("vedios")
	val vedios: List<VediosItem?>? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("dob")
	val dob: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile_no")
	val mobileNo: String? = null,

	@field:SerializedName("location")
	val location: List<LocationItem?>? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("Image")
	val image: List<ImageItem?>? = null,

	@field:SerializedName("user")
	val user: List<UserItem?>? = null,

	@field:SerializedName("email")
	val email: Any? = null
):Serializable

data class ImageItem(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("total_comments_count")
	val totalCommentsCount: Int? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("total_view_count")
	val totalViewCount: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("category_id")
	val categoryId: Int? = null,

	@field:SerializedName("media_type")
	val mediaType: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("total_like_count")
	val totalLikeCount: String? = null,

	@field:SerializedName("total_subscribe_count")
	val totalSubscribeCount: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("album_id")
	val albumId: Int? = null,

	@field:SerializedName("gallery_id")
	val galleryId: Int? = null,

	@field:SerializedName("bussines_id")
	val bussinesId: Int? = null
):Serializable

data class UserItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("profile_picture")
	val profilePicture: String? = null,

	@field:SerializedName("shop_name")
	val shopName: String? = null,

	@field:SerializedName("bussines_id")
	val bussinesId: Int? = null
):Serializable
