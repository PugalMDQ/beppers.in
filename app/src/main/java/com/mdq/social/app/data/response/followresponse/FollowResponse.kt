package com.mdq.social.app.data.response.followresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FollowResponse(

    @field:SerializedName("data")
    val data: List<DataItemses>? = null,

    @field:SerializedName("followback")
    val followback: List<followback>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Serializable

data class DataItemses(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_name")
    val user_name: String? = null,

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

    @field:SerializedName("active")
    val active: String? = null,

    @field:SerializedName("follow_request")
    val follow_request: String? = null

    )
data class followback(

    @field:SerializedName("followback")
    val followback: String? = null

    )

data class FollowerRejectItem(

    @field:SerializedName("follower_id")
    val followerId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("follow_status")
    val followStatus: Int? = null,

    @field:SerializedName("rating")
    val rating: Double? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("follower_name")
    val followerName: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Serializable

data class FollowingItem(

    @field:SerializedName("follower_id")
    val followerId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("following_name")
    val followingName: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("follow_status")
    val followStatus: Int? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Serializable

data class FollowerPendingItem(

    @field:SerializedName("follower_id")
    val followerId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("follow_status")
    val followStatus: Int? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("follower_name")
    val followerName: String? = null,

    @field:SerializedName("id")
    val id: Int? = null

) : Serializable

data class FollowerItem(

    @field:SerializedName("follower_id")
    val followerId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("follow_status")
    val followStatus: Int? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("follower_name")
    val followerName: String? = null,

    @field:SerializedName("id")
    val id: Int? = null

) : Serializable
