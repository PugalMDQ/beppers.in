package com.mdq.social.app.data.response.notification

import com.google.gson.annotations.SerializedName

data class NotificationListResponse(

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

    @field:SerializedName("from_id")
    val from_id: String? = null,

    @field:SerializedName("to_id")
    val to_id: String? = null,

    @field:SerializedName("types")
    val types: String? = null,

    @field:SerializedName("mesg")
    val mesg: String? = null,

    @field:SerializedName("read_notify")
    val read_notify: String? = null,

    @field:SerializedName("post_id")
    val post_id: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("user_name")
    val user_name: String? = null,

    @field:SerializedName("profile_photo")
    val profile_photo: String? = null,

    )

