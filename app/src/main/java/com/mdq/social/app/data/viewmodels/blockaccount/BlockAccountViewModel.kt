package com.mdq.social.app.data.viewmodels.blockaccount

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.blockaccount.BlockAccountNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class BlockAccountViewModel @Inject constructor():BaseViewModel<BlockAccountNavigator>() {


    fun getBlockList(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put(AppConstants.USER_ID, appPreference.USERID)
        api.getBlockList(hashMap)
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



    fun getBlockSearch(search:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put(AppConstants.USER_ID, appPreference.USERID)
        hashMap.put(AppConstants.USERNAME, search)
        return responseBody

    }
    fun onClick(){
        navigator.onClick()
    }
    fun unBlock(id:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("user_id", appPreference.USERID)
        hashMap.put("blocker_id", id)

        api.PostUnBlock(hashMap)
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