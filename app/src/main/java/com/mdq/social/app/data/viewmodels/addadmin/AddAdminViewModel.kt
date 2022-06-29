package com.mdq.social.app.data.viewmodels.addadmin

import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.addadmin.AddAdminNavigator
import javax.inject.Inject

class AddAdminViewModel @Inject constructor() : BaseViewModel<AddAdminNavigator>() {
    fun updateClick(){
        navigator.updateVerify()
    }
}