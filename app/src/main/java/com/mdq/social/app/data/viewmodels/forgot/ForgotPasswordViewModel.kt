package com.mdq.social.app.data.viewmodels.forgot

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.forgot.ForgotPasswordNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor() : BaseViewModel<ForgotPasswordNavigator>() {
    var mobile: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var createPassword: ObservableField<String> = ObservableField("")
    var otp: ObservableField<String> = ObservableField("")

    fun getForgot(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()

            hashMap.put(AppConstants.MOBILE, mobile.get().toString())
            hashMap.put("password", password.get().toString())

        api.getForgot(hashMap)
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

    fun loginClick(status:Int){
        navigator.loginVerify(status)
    }


}