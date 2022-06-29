package com.fasttrack.attachment.helper.upload

class EasyImageException(message: String, cause: Throwable?) : Throwable(message, cause) {
    constructor(cause: Throwable) : this(cause.message ?: "", cause)
    constructor(message: String) : this(message, null)
}