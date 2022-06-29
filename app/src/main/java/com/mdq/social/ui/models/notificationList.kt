package com.mdq.social.ui.models

class notificationList (
    /**
     * Created by sanjai on 18/03/2022
     */
     val fromId: String,
        val id: String,
        val text: String,
        val timestamp: Long,
        val toId: String,
        val type:String,
        val postID:String,
        val name:String
    ) {
        constructor() : this("", "", "", -1, "","","","")
    }
