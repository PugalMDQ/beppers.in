package com.mdq.social.app.data.viewmodels.listOfPost

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.listOfPost.ListOfPostNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class ListOfPostViewModel @Inject constructor() : BaseViewModel<ListOfPostNavigator>() {
    fun getUserProfile(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap[AppConstants.USER_ID]=userId
        hashMap["userpost"]="true"

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



}