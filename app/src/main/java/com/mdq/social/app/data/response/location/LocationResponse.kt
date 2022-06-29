package com.mdq.social.app.data.response.location

import com.google.gson.annotations.SerializedName

data class LocationResponse(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class DataItem(

    @field:SerializedName("Location_id")
    val locationId: Int? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("Location")
    val location: String? = null,

    var isSelected: Boolean? = false
)
