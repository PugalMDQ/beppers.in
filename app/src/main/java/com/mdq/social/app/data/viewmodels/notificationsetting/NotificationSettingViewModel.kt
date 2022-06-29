package com.mdq.social.app.data.viewmodels.notificationsetting

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.notificationsetting.NotificationSettingNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import com.squareup.okhttp.RequestBody
import javax.inject.Inject

class NotificationSettingViewModel @Inject constructor():BaseViewModel<NotificationSettingNavigator>() {

    fun getNotificationSetting(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getNotificationSetting(appPreference.USERID)
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

    fun setNotificationSettings(userId: String,like_comment:String,message:String,follow_request:String,follow_accept:String,user_post:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var userId: ObservableField<String> = ObservableField(userId)
        var like_comment: ObservableField<String> = ObservableField(like_comment)
        var message: ObservableField<String> = ObservableField(message)
        var follow_request: ObservableField<String> = ObservableField(follow_request)
        var follow_accept: ObservableField<String> = ObservableField(follow_accept)
        var user_post: ObservableField<String> = ObservableField(user_post)

        val hashMap = HashMap<String, okhttp3.RequestBody>()
        hashMap.put(AppConstants.USER_ID, UiUtils.convertRequestBody(userId.get().toString()))
        hashMap.put("like_comment", UiUtils.convertRequestBody(like_comment.get().toString()))
        hashMap.put("message", UiUtils.convertRequestBody(message.get().toString()))
        hashMap.put("follow_request", UiUtils.convertRequestBody(follow_request.get().toString()))
        hashMap.put("follow_accept", UiUtils.convertRequestBody(follow_accept.get().toString()))
        hashMap.put("user_post", UiUtils.convertRequestBody(user_post.get().toString()))

        api.PostNotificationSetting(hashMap)
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

    fun onClick(state:Int){
        navigator.onClick(state)
    }
}