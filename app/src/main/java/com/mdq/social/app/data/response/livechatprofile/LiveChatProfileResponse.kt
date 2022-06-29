package com.mdq.social.app.data.response.livechatprofile

import com.google.gson.annotations.SerializedName

data class LiveChatProfileResponse(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null

)

data class DataItem(

    @field:SerializedName("user_block")
    val user_block: String? = null,

    @field:SerializedName("chat_block")
    val chat_block: String? = null,

    @field:SerializedName("admin_block")
    val admin_block: String? = null,

    @field:SerializedName("deactivate")
    val deactivate: String? = null,

    @field:SerializedName("from_username")
    val from_username: String? = null,

    @field:SerializedName("from_profile")
    val from_profile: String? = null,

    @field:SerializedName("to_username")
    val to_username: String? = null,

    @field:SerializedName("to_profile")
    val to_profile: String? = null,

    @field:SerializedName("created_time")
    val created_time: String? = null,

    @field:SerializedName("read_chat")
    val read_chat: String? = null,

    @field:SerializedName("to_id")
    val to_id: String? = null,

    @field:SerializedName("from_id")
    val from_id: String? = null,

    @field:SerializedName("from_firebaseuser")
    val from_firebaseuser: String? = null,

    @field:SerializedName("to_firebaseuser")
    val to_firebaseuser: String? = null,

    @field:SerializedName("latest_message")
    val latest_message: String? = null

)

