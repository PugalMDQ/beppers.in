package com.mdq.social.app.data.viewmodels.bookmark

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.bookmark.BookMarkNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class BookMarkViewModel @Inject constructor():BaseViewModel<BookMarkNavigator>() {

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
}