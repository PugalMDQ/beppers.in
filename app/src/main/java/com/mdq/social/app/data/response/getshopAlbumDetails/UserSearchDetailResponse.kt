package com.mdq.social.app.data.response.getshopAlbumDetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserSearchDetailResponse(

    @field:SerializedName("data")
    val data: List<DataItems>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
): Serializable
data class DataItems(

    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("user_name")
    val user_name: String? = null,
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("mobile")
    val mobile: String? = null,
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("password")
    val password: String? = null,
    @field:SerializedName("dob")
    val dob: String? = null,
    @field:SerializedName("gender")
    val gender: String? = null,
    @field:SerializedName("address")
    val address: String? = null,
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
    val no_of_posts: String? = null,
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
    @field:SerializedName("like_comment")
    val like_comment: String? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("follow_request")
    val follow_request: String? = null,
    @field:SerializedName("follow_accept")
    val follow_accept: String? = null

)
