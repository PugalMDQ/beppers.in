package com.mdq.social.app.data.viewmodels.business

import android.net.Uri
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.business.BusinessProfileNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BusinessViewModel @Inject constructor() : BaseViewModel<BusinessProfileNavigator>()  {

    var upload: ObservableField<File> = ObservableField()
    var uploadmimeType: ObservableField<String> = ObservableField("")
    var path:ArrayList<File> =ArrayList<File>()
    val responseBody = MutableLiveData<AppResponse<Any>>()
    val imageList = ArrayList<MultipartBody.Part>()
    var returnUri:ArrayList<Uri> =ArrayList<Uri>()

    fun getShopTiming(userid:String):MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getShopTiming(userid)
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
    fun getRateCard(userid:String):MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.getRateCard(userid)
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
    fun DeleteRateCard(userid:String):MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        api.DeleteRateCard(userid)
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

    fun getPrivacy(userid:String):MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap["user_id"]=userid

        api.privacyGet(hashMap)
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

    fun getChatBlockStatus(userid:String):MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap["user_id"]=userid
        hashMap["follower_id"]=appPreference.USERID

        api.GetChatBlockStatus(hashMap)
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

    fun PostRateCard(userid: String,date:String): MutableLiveData<AppResponse<Any>> {
        Log.i("USERID",userid)

        if(returnUri!=null) {
            val mimeType = if (upload.get()?.getAbsolutePath()
                !!.contains("jpeg") || upload.get()?.getAbsolutePath()!!.contains("jpg")
            ) "image/*" else ""
            val uploadrequestBodys: RequestBody =
                upload.get()!!.asRequestBody(mimeType.toMediaTypeOrNull())

            val profilefileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "ratecard[]",
                    date + ".jpg",
                    uploadrequestBodys!!
                )

            api.PostRateCard(UiUtils.convertRequestBody(userid), profilefileToUpload)
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
    fun updateReview(userId: String, followerId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put(AppConstants.USER_ID, userId)
        hashMap.put(AppConstants.FOLLOWERS_id, followerId)


        api.getFollowReviews(hashMap)
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
    fun unfollow(userId: String,followerId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()
        hashMap.put(AppConstants.USER_ID, userId)
        hashMap.put(AppConstants.FOLLOWERS_id, followerId)


        api.getUnFollowReviews(hashMap)
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
    fun updateProfile(name:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        val imageList = ArrayList<MultipartBody.Part>()
        val requestBody: RequestBody = upload.get()!!.asRequestBody(uploadmimeType.get()!!.toMediaTypeOrNull())
        val fileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData("profile_photo", upload.get()!!.name, requestBody)
        imageList.add(fileToUpload)

        val hashMap = HashMap<String, RequestBody>()
        hashMap.put("user_id", UiUtils.convertRequestBody(appPreference.USERID))
        hashMap.put("name", UiUtils.convertRequestBody(name))

        api.profileUpdate(hashMap, imageList)
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

    fun getRating(post_id: String): MutableLiveData<AppResponse<Any>> {
        api.getRatings(post_id)
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

    fun getUserForFollowAndUnfollow(userId: String,followerId: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap=HashMap<String,String>()
        hashMap["id"]=userId
        hashMap["follower_id"]=followerId

        api.getFollowFollowingList(hashMap)
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

    fun profileClick() {
        navigator.profileClick()
    }

    fun insertNotification(postid: String,toid:String,name:String,type:String,message: String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()

        var hashMap = HashMap<String, RequestBody>()
        hashMap.put("from_id",UiUtils.convertRequestBody(appPreference.USERID))
        hashMap.put("to_id",UiUtils.convertRequestBody(toid))
        hashMap.put("types",UiUtils.convertRequestBody(type))
        hashMap.put("mesg",UiUtils.convertRequestBody(message))

       if(!postid.isNullOrEmpty()){
           hashMap.put("post_id",UiUtils.convertRequestBody(postid))
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