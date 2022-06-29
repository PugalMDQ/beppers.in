package com.mdq.social.app.data.response.getshopAlbumDetails

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewPostsearchDeailResponse(

    @field:SerializedName("data")
    val data: List<DataItemes>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null

): Serializable

data class DataItemes(

    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("user_id")
    val user_id: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("gallery")
    val gallery: String? = null,
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("tag")
    val tag: String? = null,
    @field:SerializedName("no_of_comments")
    val no_of_comments: String? = null,
    @field:SerializedName("no_of_likes")
    val no_of_likes: String? = null,
    @field:SerializedName("created_at")
    val created_at: String? = null,
    @field:SerializedName("user_name")
    val user_name: String? = null,
    @field:SerializedName("profile_photo")
    val profile_photo: String? = null,
    @field:SerializedName("type")
    val type: String? = null,
    @field:SerializedName("active")
    val active: String? = null,
    @field:SerializedName("bookmark_active")
    val bookmark_active: String? = null,
    @field:SerializedName("address")
    val address: String? = null,
    @field:SerializedName("taguser")
    val taguser: String? = null

    ):Serializable

