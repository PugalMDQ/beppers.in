package com.mdq.social.ui.models

/**
 * Created by sanjai on 18/03/2022
 */

class ChatMessage(
        val fromId: String,
        val id: String,
        val text: String,
        val timestamp: Long,
        val toId: String
) {
    constructor() : this("", "", "", -1, "")

}