package com.mdq.social.app.data.response.user_profile

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null

)
data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("user_name")
	val user_name: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("tag")
	val tag: String? = null,

	@field:SerializedName("gallery")
	val gallery: String? = null,

	@field:SerializedName("profile_photo")
	val profile_photo: String? = null,

	@field:SerializedName("user_id")
	val user_id: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("no_of_comments")
	val no_of_comments: String? = null,

	@field:SerializedName("no_of_likes")
	val no_of_likes: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("taguser")
	val taguser: String? = null,
)

data class ReviewsItem(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

)

data class Review(

	@field:SerializedName("reviews")
	val reviews: List<ReviewsItem?>? = null,

	@field:SerializedName("total_reviews")
	val totalReviews: Int? = null,

	@field:SerializedName("review_avg")
	val reviewAvg: Double? = null
)
