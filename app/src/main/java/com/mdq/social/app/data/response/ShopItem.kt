package com.mdq.social.app.data.response

import com.google.gson.annotations.SerializedName

data class ShopItem(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("data")
    val data: List<ShopItemItem?>? = null

)

data class ShopItemItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("user_id")
    val user_id: String? = null,

    @field:SerializedName("day")
    val day: String? = null,

    @field:SerializedName("from_time")
    val from_time: String? = null,

    @field:SerializedName("to_time")
    val to_time: String? = null
)
