package com.mdq.social.app.data.response.privacy

import com.google.gson.annotations.SerializedName

data class PrivacyDetail(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("data")
    val data: List<privacies>? = null
    )

data class privacies(

    @field:SerializedName("privacy")
    val privacy: String? = null

    )

