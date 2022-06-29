package com.mdq.social.app.data.viewmodels.livechat

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.livechat.LiveChatNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class LiveChatViewModel @Inject constructor() : BaseViewModel<LiveChatNavigator>() {

    fun InsertLiveChat(userId: String, message: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("from_id", appPreference.USERID)
        hashMap.put("to_id", userId)
        hashMap.put("messages", message)

        api.insertChat(hashMap)
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


    fun getLiveChatList(id: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("from_id", appPreference.USERID)
        hashMap.put("to_id", id)

        api.getLiveChatList(hashMap)
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

    fun ChatSeenCall(id: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put("from_id", id)
        hashMap.put("to_id", appPreference.USERID)

        //from_id - Who is sending the message
        //to_id - the one who receives the message

        api.ChatSeenCall(hashMap)
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

    fun ChatBlock(blockid: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()

        hashMap.put("chatuser_id", appPreference.USERID)
        hashMap.put("chatblocker_id", blockid)

        api.ChatBlock(hashMap)
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

    fun ChatUnBlock(blockid: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()

        hashMap.put("chatuser_id", appPreference.USERID)
        hashMap.put("chatblocker_id", blockid)

        api.ChatUnBlock(hashMap)
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