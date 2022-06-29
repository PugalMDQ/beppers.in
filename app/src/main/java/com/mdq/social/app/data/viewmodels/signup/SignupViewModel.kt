package com.mdq.social.app.data.viewmodels.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.signup.SignUpNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class SignupViewModel @Inject constructor() : BaseViewModel<SignUpNavigator>()  {

    var name: ObservableField<String> = ObservableField("")
    var username: ObservableField<String> = ObservableField("")
    var useremail: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var dod: ObservableField<String> = ObservableField("")
    var mobilenumber: ObservableField<String> = ObservableField("")
    var profileFile: ObservableField<File> = ObservableField()
    var profilemimeType: ObservableField<String> = ObservableField("")
    var services: ObservableField<String> = ObservableField("")
    var firebase_UID: ObservableField<String> = ObservableField("")


    fun signUp(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        val hashMap = HashMap<String, RequestBody>()
        hashMap.put("user_name", UiUtils.convertRequestBody(username.get().toString()))
        hashMap.put("name", UiUtils.convertRequestBody(name.get().toString()))
        hashMap.put("email",UiUtils.convertRequestBody(useremail.get().toString()) )
        hashMap.put("password",UiUtils.convertRequestBody(password.get().toString()) )
        hashMap.put("dob",  UiUtils.convertRequestBody(" "))
        hashMap.put("mobile", UiUtils.convertRequestBody(mobilenumber.get().toString()))
        hashMap.put("type",UiUtils.convertRequestBody(AppConstants.USER_TYPE_INDIVIDUAL) )
        hashMap.put("gender",UiUtils.convertRequestBody("male"))
        hashMap.put("firebase_userid",UiUtils.convertRequestBody(firebase_UID.get().toString()))

        api.getSignUpUser(hashMap)
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

    fun UpdateFireBase(userid:String,FireBaseUid:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, RequestBody>()
        hashMap.put("user_id", UiUtils.convertRequestBody(userid))
        hashMap.put("firebase_userid", UiUtils.convertRequestBody(FireBaseUid))

        api.setFireBaseUID(hashMap)
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



    fun backOnClick(){
        navigator. backOn()
    }

    fun loginClick(){
        navigator. login()
    }

    fun signUpClick(){
        navigator. signUp()
    }

    fun dobclick(){
        navigator. dob()
    }

    fun profileClick(){
        navigator. profile()
    }
}