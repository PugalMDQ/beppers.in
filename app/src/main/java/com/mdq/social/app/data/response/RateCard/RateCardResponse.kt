package com.mdq.social.app.data.response.RateCard

import com.google.gson.annotations.SerializedName


data class RateCardResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("data")
    val data: List<DataItem>? = null

    )

data class DataItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_id")
    val user_id: String? = null,

    @field:SerializedName("post_id")
    val post_id: String? = null,

    @field:SerializedName("ratecard")
    val ratecard: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null

)

