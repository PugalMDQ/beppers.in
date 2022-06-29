package com.mdq.social.app.data.response.signup

import com.google.gson.annotations.SerializedName

data class SingUpBusinessResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: String? = null,

    @field:SerializedName("errors")
    val errors: String? = null,

    @field:SerializedName("data")
    val data: dataForBusinessResponse? = null

)

data class dataForBusinessResponse(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("shop_name")
    val shop_name: String? = null,

    @field:SerializedName("mobile_no")
    val mobile_no: String? = null,

    @field:SerializedName("service_for")
    val service_for: String? = null,

    @field:SerializedName("certification_proof")
    val certification_proof: String? = null,

    @field:SerializedName("updated_at")
    val updated_at: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("id")
    val id: Int? = null

)
