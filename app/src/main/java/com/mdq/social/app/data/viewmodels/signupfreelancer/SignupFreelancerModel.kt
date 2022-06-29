package com.mdq.social.app.data.viewmodels.signupfreelancer


import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.signupfreelancer.SignupFreelancerNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class SignupFreelancerModel  @Inject constructor() : BaseViewModel<SignupFreelancerNavigator>()  {
    var name: ObservableField<String> = ObservableField("")
    var username: ObservableField<String> = ObservableField("")
    var useremail: ObservableField<String> = ObservableField("")
    var password: ObservableField<String> = ObservableField("")
    var address: ObservableField<String> = ObservableField("")
    var mobilenumber: ObservableField<String> = ObservableField("")
    var categories : ObservableField<String> = ObservableField("")
    var services: ObservableField<String> = ObservableField("")
    var city: ObservableField<String> = ObservableField("")
    var description: ObservableField<String> = ObservableField("")
    var pincode: ObservableField<String> = ObservableField("")
    var profiles: ObservableField<File> = ObservableField()
    var profilemimeType: ObservableField<String> = ObservableField("")
    var upload: ObservableField<File> = ObservableField()
    var uploadmimeType: ObservableField<String> = ObservableField("")
    var travel: ObservableField<String> = ObservableField("")
    var firebase_UID: ObservableField<String> = ObservableField("")

    fun signUp(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var uploadfileToUpload: MultipartBody.Part?=null

        if (!upload.get()?.name.isNullOrEmpty()) {
            val uploadrequestBody: RequestBody =
                upload.get()!!.asRequestBody(uploadmimeType.get()!!.toMediaTypeOrNull())
            uploadfileToUpload =
                MultipartBody.Part.createFormData(
                    AppConstants.CERTIFICATE,
                    upload.get()!!.name,
                    uploadrequestBody
                )
        }

        val hashMap = HashMap<String, RequestBody>()
        hashMap.put("type", UiUtils.convertRequestBody(AppConstants.USER_TYPE_FREELACER))
        hashMap.put("user_name", UiUtils.convertRequestBody(username.get().toString()))
        hashMap.put("name", UiUtils.convertRequestBody(name.get().toString()))
        hashMap.put("email", UiUtils.convertRequestBody(useremail.get().toString()))
        hashMap.put("password",UiUtils.convertRequestBody(password.get().toString()))
        hashMap.put("mobile",UiUtils.convertRequestBody(mobilenumber.get().toString()))
        hashMap.put("address",UiUtils.convertRequestBody(address.get().toString()))
        hashMap.put("description",UiUtils.convertRequestBody(description.get().toString()))
        hashMap.put("categories",UiUtils.convertRequestBody(categories.get().toString()))
        hashMap.put("service",UiUtils.convertRequestBody(services.get().toString()))
        hashMap.put("dob",UiUtils.convertRequestBody(" "))
        hashMap.put("travel",UiUtils.convertRequestBody(travel.get().toString()))
        hashMap.put("firebase_userid",UiUtils.convertRequestBody(firebase_UID.get().toString()))


        api.getSignUp(hashMap,uploadfileToUpload!!)
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


    fun onSignUpClick(){
        navigator.onSignupClick()
    }

    fun onCategoryClick(){
        navigator.onCategoryClick()
    }

    fun getCategory(): MutableLiveData<AppResponse<Any>>{
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getCategory()
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

    fun onBackClick(){
        navigator.onBackClick()
    }

    fun onUploadClick(status:Int) {
        navigator.onUploadClick(status)
    }

    fun onLocationClick() {
        navigator.onLocationClick()
    }

    fun profileClick(status:Int) {
        navigator.profileClick(status)
    }

    fun onCancelLocationClick() {
        navigator.onCancelLocationClick()
    }
    fun onConfirmLocationClick() {
        navigator.onConfirmLocationClick()
    }

    fun onCancelClick() {
        navigator.onCancelClick()
    }
    fun onConfirmClick() {
        navigator.onConfirmClick()
    }

}