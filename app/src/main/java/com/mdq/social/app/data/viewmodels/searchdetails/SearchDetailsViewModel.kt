package com.mdq.social.app.data.viewmodels.searchdetails

import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.searchdetails.SearchDetailsNavigator
import com.mdq.social.utils.RxJavaUtils
import javax.inject.Inject

class SearchDetailsViewModel @Inject constructor():BaseViewModel<SearchDetailsNavigator>() {

    fun getCategory(): MutableLiveData<AppResponse<Any>>{
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getCategory()
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

 fun getSearchFilterUserDetails(city: String,area:String,service_for:String): MutableLiveData<AppResponse<Any>>
    {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        var hashMap=HashMap<String,String>()

        hashMap["city"]=city
        hashMap["area"]=area
        hashMap["category"]=service_for
        hashMap["user_id"]=appPreference.USERID

        api.getSearchFilterUserDetails(hashMap)
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

    fun getSearchUserDetails(city: String,postsearch:String,area:String,category:String): MutableLiveData<AppResponse<Any>>
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

    fun viewAllClick(viewall:Int){
        navigator.viewAllClick(viewall)
    }

}