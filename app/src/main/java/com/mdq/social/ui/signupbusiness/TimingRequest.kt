package com.mdq.social.ui.signupbusiness

import com.google.gson.annotations.SerializedName

data class TimingRequest(

    @field:SerializedName("shop_time")
    var shopTime: List<TimingItem>? = null
)

data class TimingItem(

    @field:SerializedName("day")
    var day: String? = null,

    @field:SerializedName("from")
    var morningTime: String? = null,

    @field:SerializedName("to")
    var eveningTime: String? = null

)
