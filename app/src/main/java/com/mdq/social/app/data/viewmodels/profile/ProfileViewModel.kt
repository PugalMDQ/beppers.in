package com.mdq.social.app.data.viewmodels.profile

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.profile.ProfileNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : BaseViewModel<ProfileNavigator>() {
    var upload: ObservableField<File> = ObservableField()
    var uploadmimeType: ObservableField<String> = ObservableField("")

    fun getUserProfile(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap = HashMap<String, String>()
        hashMap[AppConstants.USER_ID] = userId

        api.getUserProfile(userId,"true")
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

    fun updateReview(userId: String, followStatus: String, rating: String, followerId: String, review: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put(AppConstants.USER_ID, userId)
        hashMap.put(AppConstants.FOLLOWERS_id, followerId)

        api.getFollowReviews(hashMap)
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

    fun updateProfile(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        val imageList = ArrayList<MultipartBody.Part>()
        val requestBody: RequestBody = upload.get()!!.asRequestBody(uploadmimeType.get()!!.toMediaTypeOrNull())
        val fileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData(AppConstants.PROFILEPICTURE, upload.get()!!.name, requestBody)
        imageList.add(fileToUpload)

        val hashMap = HashMap<String, RequestBody>()
        hashMap.put(AppConstants.USER_ID, UiUtils.convertRequestBody(appPreference.USERID))

        api.profileUpdate(hashMap, imageList)
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

    fun profileClick(status:Int) {
        navigator.profileClick(status)
    }

    fun onClick(status:Int) {
        navigator.onClick(status)
    }

}