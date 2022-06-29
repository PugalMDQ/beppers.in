package com.mdq.social.ui.signupbusiness

import com.google.gson.annotations.SerializedName

data class selected_category_request(

    @field:SerializedName("selected_categories")
    var selected_categories: List<String>? = null
)

