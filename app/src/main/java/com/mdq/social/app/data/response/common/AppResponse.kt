package com.mdq.social.app.data.response.common

import com.mdq.social.app.data.response.common.ResponseStatus.ERROR
import com.mdq.social.app.data.response.common.ResponseStatus.SUCCESS

class AppResponse<T> private constructor(val status: String, val data: T?, val throwable: Throwable?) {
    companion object {

        fun <T> success(data: T): AppResponse<T> {
            return AppResponse(SUCCESS, data, null)
        }

        fun <T> error(error: Throwable): AppResponse<T> {
            return AppResponse(ERROR, null, error)
        }
    }

}