package com.mdq.social.app.data.viewmodels.login

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.response.login.LoginResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.login.LoginNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class LoginViewModel  @Inject constructor() : BaseViewModel<LoginNavigator>()  {

    var username: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var token: ObservableField<String> = ObservableField("")

    fun login(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("username", username.get().toString())
        hashMap.put("password", password.get().toString())
        api.getLogin(hashMap)
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

    fun intiateLoginPreference(loginResponse: LoginResponse) {
        appPreference.USERID = loginResponse.data?.get(0)?.id.toString()
        appPreference.USERGROUP = loginResponse.data?.get(0)?.type.toString()
    }

    fun loginClick(){
        navigator.loginVerify()
    }

    fun forgotClick(){
        navigator.forgotClick()
    }

    fun signupClick(){
        navigator.signup()
    }

}