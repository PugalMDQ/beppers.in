package com.mdq.social.app.data.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: String? = null

)

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("user_name")
	val user_name: String? = null,

	@field:SerializedName("type")
	val type: String? = null

)

data class userItem(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user_type")
	val user_type: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("mobile_no")
	val mobile_no: String? = null,

	@field:SerializedName("updated_at")
	val updated_at: String? = null,

	@field:SerializedName("created_at")
	val created_at: String? = null
)







