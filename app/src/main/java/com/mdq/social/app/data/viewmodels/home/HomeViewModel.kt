package com.mdq.social.app.data.viewmodels.home

import androidx.lifecycle.MutableLiveData
import okhttp3.RequestBody
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.home.HomeNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel<HomeNavigator>() {

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

    fun UpdateLikeUnlike(PoastId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap = HashMap<String, String>()
        hashMap[AppConstants.ALBUM_ID] = PoastId

        api.getLikeList(PoastId)
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

    fun getAddLikeComments(
        PoastId: String,
        userID: String,
        id: String
    ): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        if (!appPreference.USERID.equals(userID)) {
            var hashMap = HashMap<String, String>()
            hashMap[AppConstants.ALBUM_ID] = PoastId
            hashMap[AppConstants.USER_ID] = appPreference.USERID

            api.getAddLike(hashMap)
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
        }
            return responseBody
        }

    fun getUserProfileDetails(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap[AppConstants.USER_ID]=userId

        api.getUserProfileDetails(userId)
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

    fun getRating(user_id: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getRatings(user_id)
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

    fun SaveBookmark(post_id: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.SaveBook(appPreference.USERID,post_id)
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

    fun getAddUnLikeComments(
            PoastId: String,
            userID: String,
            id: String
        ): MutableLiveData<AppResponse<Any>> {
            val responseBody = MutableLiveData<AppResponse<Any>>()
            if(!appPreference.USERID.equals(userID)) {
                var hashMap = HashMap<String, String>()
                hashMap[AppConstants.ALBUM_ID] = PoastId
                hashMap[AppConstants.USER_ID] = appPreference.USERID
                api.getAddUnLike(hashMap)
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
            }
            return responseBody
        }

    fun getHidePost(albumId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        if(!appPreference.USERID.equals(albumId)) {
            var hashMap = HashMap<String, String>()
            hashMap["post_id"] = albumId
            hashMap["user_id"] = appPreference.USERID
            api.hidePost(hashMap)
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
        }
        return responseBody
    }

    fun deletePost(postid: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.DeletePost(postid,appPreference.USERID)
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

    fun insertNotification(postid: String,toid:String,name:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap = HashMap<String, RequestBody>()
        hashMap.put("from_id",UiUtils.convertRequestBody(appPreference.USERID))
        hashMap.put("to_id",UiUtils.convertRequestBody(toid))
        hashMap.put("types",UiUtils.convertRequestBody("like"))
        hashMap.put("mesg",UiUtils.convertRequestBody("has liked your post"))

        if(!postid.isNullOrEmpty()) {
            hashMap.put("post_id", UiUtils.convertRequestBody(postid))
        }

        api.insertNotification(hashMap)
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

}