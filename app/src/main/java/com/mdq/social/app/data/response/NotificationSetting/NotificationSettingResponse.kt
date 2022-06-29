package com.mdq.social.app.data.response.NotificationSetting

import com.google.gson.annotations.SerializedName

data class NotificationSettingResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("data")
    val data: List<notificationSetting>? = null
)

data class notificationSetting(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("like_comment")
    val like_comment: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("follow_request")
    val follow_request: String? = null,

    @field:SerializedName("user_post")
    val user_post: String? = null,

    @field:SerializedName("follow_accept")
    val follow_accept: String? = null

    )

