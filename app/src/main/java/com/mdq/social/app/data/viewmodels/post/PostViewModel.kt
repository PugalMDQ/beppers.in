package com.mdq.social.app.data.viewmodels.post

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.post.PostNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.RequestBody
import javax.inject.Inject

class PostViewModel @Inject constructor() : BaseViewModel<PostNavigator>()  {

    var like: ObservableField<String> = ObservableField("")
    var comment: ObservableField<String> = ObservableField("")
    var subscribe: ObservableField<String> = ObservableField("")

    fun getCommentDetails(albumId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getCommentDetails(albumId)
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

    fun getCommentBlockList(User_id: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap["block_user_id"]=appPreference.USERID
        hashMap["user_id"]= User_id

        api.getCommentBlockListForComment(hashMap)
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

    fun getAddComment(albumId: String,userID:String,comments:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()

        hashMap[AppConstants.USER_ID]=userID
        hashMap[AppConstants.ALBUM_ID]=albumId
        hashMap[AppConstants.COMMENTS]=comments


        api.getAddComment(hashMap)
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

    fun getSubAddComment(albumId: String,userID:String,subcommentsid:String,comments:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()

        hashMap[AppConstants.USER_ID]=userID
        hashMap[AppConstants.ALBUM_ID]=albumId
        hashMap[AppConstants.COMMENTS]=comments
        hashMap[AppConstants.SUBCOMMENTSID]=subcommentsid


        return responseBody

    }

    fun insertNotification(postid: String,toid:String,name:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap = HashMap<String, RequestBody>()
        hashMap.put("from_id",UiUtils.convertRequestBody(appPreference.USERID))
        hashMap.put("to_id",UiUtils.convertRequestBody(toid))
        hashMap.put("types",UiUtils.convertRequestBody("Comment"))
        hashMap.put("mesg",UiUtils.convertRequestBody(" has comment your post"))
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

}