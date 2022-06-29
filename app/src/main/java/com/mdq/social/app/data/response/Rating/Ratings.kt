package com.mdq.social.app.data.response.Rating

import com.google.gson.annotations.SerializedName

data class Ratings(
    @field:SerializedName("data")
    val data: List<DataItemses>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null

)

data class DataItemses(
    @field:SerializedName("avg_rating")
    val avg_rating: String? = null
)
