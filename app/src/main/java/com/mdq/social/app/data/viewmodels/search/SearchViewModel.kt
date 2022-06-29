package com.mdq.social.app.data.viewmodels.search

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.search.SearchNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class SearchViewModel @Inject constructor() : BaseViewModel<SearchNavigator>() {

    fun getCategory(): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()


        return responseBody
    }

    fun getRecent(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getRecent(userId)
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

    fun getAddLikeComments(albumId: String,userID:String,view:String,like:String,subscribe:String,comments:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        val hashMap=HashMap<String,String>()

        hashMap[AppConstants.ALBUM_ID]=albumId
        hashMap[AppConstants.USER_ID]=userID
        hashMap[AppConstants.VIEW]=view
        hashMap[AppConstants.LIKE]=like
        hashMap[AppConstants.SUBSCRIBE]=subscribe
        hashMap[AppConstants.COMMENTS]=comments



        return responseBody
    }

    fun searchClick(){
        navigator.searchClick()
    }
}