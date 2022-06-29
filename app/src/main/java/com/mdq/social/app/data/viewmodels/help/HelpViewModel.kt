package com.mdq.social.app.data.viewmodels.help

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.help.HelpNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class HelpViewModel @Inject constructor():BaseViewModel<HelpNavigator>() {

    fun HelpSupport(name: String,number:String,email: String,message:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap = HashMap<String, String>()
        hashMap["name"] = name
        hashMap["number"] = number
        hashMap["email"] = email
        hashMap["message"] = message

        api.HelpAndSupport(hashMap)
            .compose(RxJavaUtils.applyObserverSchedulers())
            .compose(RxJavaUtils.applyErrorTransformer())
            .doOnSubscribe { loadingStatus.value = true }
            .doOnTerminate { loadingStatus.value = false }
            .subscribe({ response ->
                if (response != null) {
                    responseBody.value = AppResponse.success(response)
                }
            }, { throwable ->
                responseBody.value = AppResponse.error(throwable)
            })

        return responseBody
    }

    fun getUserProfileDetails(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap[AppConstants.USER_ID]=userId

        api.getUserProfileDetails(userId)
            .compose(RxJavaUtils.applyObserverSchedulers())
            .compose(RxJavaUtils.applyErrorTransformer())
            .doOnSubscribe { loadingStatus.value = true }
            .doOnTerminate { loadingStatus.value = false }
            .subscribe({ response ->
                if (response != null) {
                    responseBody.value = AppResponse.success(response)
                }
            }, { throwable ->
                responseBody.value = AppResponse.error(throwable)
            })

        return responseBody
    }

    fun onClick(){
        navigator.onClick()
    }
}