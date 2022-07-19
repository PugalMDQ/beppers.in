package com.mdq.social.app.data.viewmodels.notification

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.notification.NotificationNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.RequestBody
import javax.inject.Inject

class NotificationViewModel @Inject constructor() :BaseViewModel<NotificationNavigator>() {

    fun getUserProfile(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()


        var hashMap=HashMap<String,String>()
        hashMap["userid"]=userId
        hashMap["userpost"]="true"
        if(userId!=appPreference.USERID) {
            hashMap["visitorid"] = appPreference.USERID
        }

        api.getUserProfileForPostScreen(hashMap)
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

    fun getNotificationList(userId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getNotificationList(userId)
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


    fun getBookmark(): MutableLiveData<AppResponse<Any>> {

        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("user_id", appPreference.USERID)
        api.getBookMarklist(hashMap)
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

    fun getSharedPost(postid: String?): MutableLiveData<AppResponse<Any>> {

        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("user_id", appPreference.USERID)
        hashMap.put("post_id", postid!!)
        api.getShredPostlist(hashMap)
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

    fun insertNotification(postid: String,toid:String,name:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap = HashMap<String, RequestBody>()

        hashMap.put("from_id",UiUtils.convertRequestBody(appPreference.USERID))
        hashMap.put("to_id",UiUtils.convertRequestBody(toid))
        hashMap.put("types",UiUtils.convertRequestBody("like"))
        hashMap.put("mesg",UiUtils.convertRequestBody("has liked your post"))
        if (!postid.isNullOrEmpty()) {
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


    fun readNotificationCall(
        id: String
    ): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.readNotificationCall(id)
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
    fun UnLike(
        PoastId: String,
        userID: String,
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



    fun AddLike(
        PoastId: String,
        userID: String,
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
    fun getSearchDetails(postsearch:String): MutableLiveData<AppResponse<Any>>
    {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap=HashMap<String,String>()

        hashMap["key"]=postsearch
        hashMap["user_id"]=appPreference.USERID

        api.getSearchPostDetails(hashMap)
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

    fun getSearchFilterPostDetails(city: String,area:String,service_for:String): MutableLiveData<AppResponse<Any>>
    {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap=HashMap<String,String>()

        hashMap["city"]=city
        hashMap["area"]=area
        hashMap["category"]=service_for
        hashMap["user_id"]=appPreference.USERID

        api.getSearchFilterPostDetails(hashMap)
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

}