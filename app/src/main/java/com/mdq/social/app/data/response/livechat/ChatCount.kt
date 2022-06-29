package com.mdq.social.app.data.response.livechat

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatCount(

    @field:SerializedName("data")
    val data: List<count>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null
) : Serializable

data class count(

    @field:SerializedName("from_id")
    val from_id: String? = null,

    @field:SerializedName("Count")
    val Count: String? = null,
)
