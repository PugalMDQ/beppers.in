package com.mdq.social.app.data.response.FollowandUnfollowResponce

import com.google.gson.annotations.SerializedName

data class FollowAndUndFollowResponse (


    @field:SerializedName("data")
    val data: List<DataItem>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null
)

data class DataItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_name")
    val user_name: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("mobile")
    val mobile: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("address")
    val address: Int? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("categories")
    val categories: String? = null,

    @field:SerializedName("others")
    val others: String? = null,

    @field:SerializedName("service")
    val service: String? = null,

    @field:SerializedName("certificate_proof")
    val certificate_proof: String? = null,

    @field:SerializedName("no_of_posts")
    val no_of_posts: Int? = null,

    @field:SerializedName("no_of_following")
    val no_of_following: String? = null,

    @field:SerializedName("no_of_followers")
    val no_of_followers: String? = null,

    @field:SerializedName("profile_photo")
    val profile_photo: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("updated_at")
    val updated_at: String? = null,

    )