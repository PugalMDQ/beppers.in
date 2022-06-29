package com.mdq.social.app.data.viewmodels.blockcomment

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.blockcomment.BlockCommentNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class BlockCommentViewModel @Inject constructor():BaseViewModel<BlockCommentNavigator>() {

    fun getSearchUserDetails(postsearch:String): MutableLiveData<AppResponse<Any>>
    {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap=HashMap<String,String>()

        hashMap["key"]=postsearch
        hashMap["user_id"]=appPreference.USERID

        api.getSearchUserDetails(hashMap)
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

 fun CommentBlock(blockid:String): MutableLiveData<AppResponse<Any>>
    {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap=HashMap<String,String>()

        hashMap["block_user_id"]=blockid
        hashMap["user_id"]=appPreference.USERID

        api.CommentBlock(hashMap)
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

    fun CommentUNBlock(blockid:String): MutableLiveData<AppResponse<Any>>
    {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap=HashMap<String,String>()

        hashMap["block_user_id"]=blockid
        hashMap["user_id"]=appPreference.USERID

        api.CommentUNBlock(hashMap)
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
        hashMap[AppConstants.USER_ID]=User_id

        api.getCommentBlockList(hashMap)
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


    fun onClick(status:Int){
        navigator.onClick(status)
    }
}