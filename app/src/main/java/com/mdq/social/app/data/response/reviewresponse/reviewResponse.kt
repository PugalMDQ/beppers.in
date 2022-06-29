package com.mdq.social.app.data.response.reviewresponse

import com.google.gson.annotations.SerializedName

data class reviewResponse(
    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_name")
    val user_name: String? = null,

    @field:SerializedName("profile_photo")
    val profile_photo: String? = null,

    @field:SerializedName("review_user_id")
    val review_user_id: String? = null,

    @field:SerializedName("rating")
    val rating: String? = null,

    @field:SerializedName("reviews")
    val reviews: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    )

