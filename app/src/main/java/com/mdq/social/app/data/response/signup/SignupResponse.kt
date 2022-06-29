package com.mdq.social.app.data.response.signup

import com.google.gson.annotations.SerializedName

data class SignupResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("data")
    val data: data? = null

)

data class data(
    @field:SerializedName("userid")
    val userid: String? = null
)