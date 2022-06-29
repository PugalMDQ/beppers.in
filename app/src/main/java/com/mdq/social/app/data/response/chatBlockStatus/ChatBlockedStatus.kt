package com.mdq.social.app.data.response.chatBlockStatus

import com.google.gson.annotations.SerializedName

class ChatBlockedStatus {

    @field:SerializedName("message")
    val message: String? = null

    @field:SerializedName("error")
    val error: Boolean? = null


    @field:SerializedName("data")
    val data: List<data?>? = null

}
data class data(

    @field:SerializedName("chat_block")
    val chat_block: String? = null

)
