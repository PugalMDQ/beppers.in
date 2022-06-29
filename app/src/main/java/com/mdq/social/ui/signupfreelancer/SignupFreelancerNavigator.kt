package com.mdq.social.ui.signupfreelancer

interface SignupFreelancerNavigator {
    fun onBackClick()
    fun onSignupClick()
    fun onCategoryClick()

    fun onLocationClick()
    fun onUploadClick(status: Int)
    fun profileClick(status: Int)

    fun onCancelClick()
    fun onConfirmClick()
    fun onCancelLocationClick()
    fun onConfirmLocationClick()
}