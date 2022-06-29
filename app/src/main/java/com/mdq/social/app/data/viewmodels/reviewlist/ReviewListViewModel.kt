package com.mdq.social.app.data.viewmodels.reviewlist

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.reviewlist.ReviewListNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class ReviewListViewModel @Inject constructor():BaseViewModel<ReviewListNavigator>() {

    fun getReviewList(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap=HashMap<String,String>()
        hashMap[AppConstants.USER_ID]=userId

        api.getReviewList(hashMap)
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

//    fun getid(): MutableLiveData<AppResponse<Any>> {
//        val responseBody = MutableLiveData<AppResponse<Any>>()
//
//        api.getTEST()
//                .compose(RxJavaUtils.applyObserverSchedulers())
//                .compose(RxJavaUtils.applyErrorTransformer())
//                .doOnSubscribe { loadingStatus.value = true }
//                .doOnTerminate { loadingStatus.value = false }
//                .subscribe({ response ->
//                    if (response != null) {
//                        responseBody.value = AppResponse.success(response)
//                    }
//                }, { throwable ->
//                    responseBody.value = AppResponse.error(throwable)
//                })
//
//        return responseBody
//    }

    fun PostReview(userId: String,review:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap=HashMap<String,String>()
        hashMap[AppConstants.USER_ID]=appPreference.USERID
        hashMap["review_user_id"]=userId
        hashMap["reviews"]=review

        api.PostReview(hashMap)
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

    fun PostRating(userId: String,rating:String,review:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap=HashMap<String,String>()
        hashMap[AppConstants.USER_ID]=appPreference.USERID
        hashMap["review_user_id"]=userId
        hashMap["rating"]=rating
        hashMap["review"]=review

        api.PostRating(hashMap)
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