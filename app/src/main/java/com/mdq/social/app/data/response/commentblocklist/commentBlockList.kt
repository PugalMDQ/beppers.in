package com.mdq.social.app.data.response.commentblocklist

import com.google.gson.annotations.SerializedName

data class commentBlockList(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null

)
data class DataItem(


    @field:SerializedName("user_id")
    val user_id: String? = null,

    @field:SerializedName("block_user_id")
    val block_user_id: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("user_name")
    val user_name: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("profile_photo")
    val profile_photo: String? = null

)
