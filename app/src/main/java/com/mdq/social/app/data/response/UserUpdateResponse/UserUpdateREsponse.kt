package com.mdq.social.app.data.response.UserUpdateResponse

import com.google.gson.annotations.SerializedName

data class UserUpdateREsponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null
)