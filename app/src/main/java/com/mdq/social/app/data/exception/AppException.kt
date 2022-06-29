package com.mdq.social.app.data.exception

import retrofit2.Response

import java.io.IOException

class AppException(detailMessage: String) : IOException(detailMessage) {
    companion object {

        fun isAppException(response: Response<*>): Boolean {
            return response.code() >= 400 && response.code() < 500
        }

        @Throws(IOException::class)
        fun create(response: Response<*>): AppException {
            return AppException(response.errorBody()!!.string())
        }
    }

}