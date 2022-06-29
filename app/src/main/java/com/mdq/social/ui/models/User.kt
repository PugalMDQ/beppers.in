package com.mdq.social.ui.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by sanjai on 18/03/2022
 */
@Parcelize
data class User(
        val uid: String,
        val name: String,
        val profileImageUrl: String?,
        val mobileno: String?,
        val userID:String?
) : Parcelable {
    constructor() : this("", "", "","","")
}