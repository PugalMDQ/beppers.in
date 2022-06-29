package com.mdq.social.app.data.response.terms

import com.google.gson.annotations.SerializedName

data class TermsResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("about_us")
	val aboutUs: String? = null,

	@field:SerializedName("terms_and_condtion")
	val termsAndCondtion: String? = null
)
