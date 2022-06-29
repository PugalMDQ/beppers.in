package com.mdq.social.app.data.response.CommentResponse

import com.google.gson.annotations.SerializedName

data class CommentResponse  (

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

    @field:SerializedName("user_id")
    val user_id: String? = null,

    @field:SerializedName("post_id")
    val post_id: String? = null,

    @field:SerializedName("comments")
    val comments: String? = null,

    @field:SerializedName("active")
    val active: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

  @field:SerializedName("user_name")
    val user_name: String? = null,

  @field:SerializedName("profile_photo")
    val profile_photo: String? = null
)

