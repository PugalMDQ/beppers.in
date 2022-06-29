package com.mdq.social.ui.businessupdate

interface BusinessUpdateNavigator {
    fun onBackClick()


    fun onCategoriesClick()
    fun onUpdateClick()

    fun onUploadClick(status:Int)

    fun onLocationClick()

    fun onDayStartClick(status:Int)
    fun onDayEndClick(status:Int)
    fun profileClick(status: Int)
    fun onCancelClick()
    fun onConfirmClick()
    fun onConfirmLocationClick()
    fun onCancelLocationClick()
    fun editClick()
}