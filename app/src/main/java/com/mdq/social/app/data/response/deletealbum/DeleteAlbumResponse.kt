package com.mdq.social.app.data.response.deletealbum

import com.google.gson.annotations.SerializedName

data class DeleteAlbumResponse(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
