package com.mdq.social.ui.signupbusiness

interface SignupBusinessNavigator {


    fun onBackClick()


    fun onCategoriesClick()
    fun onSignupClick()

    fun onUploadClick(status:Int)

    fun onLocationClick()

    fun onDayStartClick(status:Int)
    fun onDayEndClick(status:Int)
    fun profileClick(status: Int)
    fun onCancelClick()
    fun onConfirmClick()
    fun onConfirmLocationClick()
    fun onCancelLocationClick()
}