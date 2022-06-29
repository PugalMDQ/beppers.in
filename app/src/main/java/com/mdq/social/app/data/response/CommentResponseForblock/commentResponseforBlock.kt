package com.mdq.social.app.data.response.CommentResponseForblock

import com.google.gson.annotations.SerializedName

data class CommentresponseforBlock  (

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

    @field:SerializedName("block_user_id")
    val block_user_id: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null

)
