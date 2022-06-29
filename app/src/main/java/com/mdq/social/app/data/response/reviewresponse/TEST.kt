package com.mdq.social.app.data.response.reviewresponse

import com.google.gson.annotations.SerializedName

data class TESTt(
    @field:SerializedName("result")
    val result: Array<String> ?= null,

    @field:SerializedName("type")
    val type: String? = null
)