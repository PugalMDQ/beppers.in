package com.mdq.social.app.data.response.category

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("errors")
    val errors: String? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null

)

data class DataItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    var isSelected: Boolean? = false

)
