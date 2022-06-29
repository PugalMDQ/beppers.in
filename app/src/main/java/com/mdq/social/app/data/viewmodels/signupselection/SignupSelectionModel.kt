package com.mdq.social.app.data.viewmodels.signupselection

import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.signupselection.SignupSelectionNavigator
import javax.inject.Inject

class SignupSelectionModel  @Inject constructor() : BaseViewModel<SignupSelectionNavigator>()  {

    fun onBackClick(){
        navigator.onBackClick()
    }

    fun onIndividualClick(){
        navigator.onIndividualClick()
    }

    fun onBusinessClick(){
        navigator.onBusinessClick()
    }

    fun onFreelanceClick(){
        navigator.onFreelancerClick()
    }
}