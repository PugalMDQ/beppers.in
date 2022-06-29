package com.mdq.social.app.data.viewmodels.addpost

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.mdq.social.app.data.response.common.AppResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.ui.addpost.AddPostNavigator
import com.mdq.social.utils.RxJavaUtils
import com.mdq.social.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddPostViewModel @Inject constructor() : BaseViewModel<AddPostNavigator>()  {
    var thumb: ObservableField<File> = ObservableField()
    var path:ArrayList<File> =ArrayList<File>()
    var returnUri:ArrayList<Uri> =ArrayList<Uri>()

    fun updatePost(userId:String,galleryId:String,title:String,description:String,tagId:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val imageList:ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
        var thamrequestBody: RequestBody?= null
        var profilerequestBody: RequestBody?=null

        if(returnUri!=null) {
            for (i in returnUri.indices!!) {
                val mimeType = if (path.get(i).getAbsolutePath()
                        .contains("jpeg") || path.get(i).getAbsolutePath().contains("jpg")
                ) "image/*" else ""
                val uploadrequestBodys: RequestBody =
                    path.get(i)!!.asRequestBody(mimeType.toMediaTypeOrNull())

                val profilefileToUpload: MultipartBody.Part =
                    MultipartBody.Part.createFormData(
                        "gallery[]",
                        userId+galleryId+i+".jpeg",
                        uploadrequestBodys!!
                    )
                imageList.add(profilefileToUpload)
            }
        }

        if(thumb.get()!=null) {
                if (!thumb.get()?.name.isNullOrEmpty()) {
                    val thamrequestBody: RequestBody = thumb.get()?.asRequestBody("video/mp4".toMediaTypeOrNull())!!
                    val tambfileToUpload: MultipartBody.Part =
                        MultipartBody.Part.createFormData("gallery[]",userId+galleryId+".mp4",thamrequestBody)
                    imageList.add(tambfileToUpload)

                }
            }

        val hashMap = HashMap<String, RequestBody>()
        hashMap.put("name", UiUtils.convertRequestBody(title))
        hashMap.put("description",UiUtils.convertRequestBody(description))
        hashMap.put("user_id",UiUtils.convertRequestBody(userId))
        if(!tagId.equals("0")){
            hashMap.put("tag",UiUtils.convertRequestBody(tagId))
        }else{
            hashMap.put("tag",UiUtils.convertRequestBody("tagId"))
        }

        if(imageList!=null) {
            api.updatePost(hashMap, imageList)
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

    fun getfollowFollowingList(userId:String): MutableLiveData<AppResponse<Any>> {
        val responseBody = MutableLiveData<AppResponse<Any>>()
        val hashMap = HashMap<String, String>()

        hashMap.put("follower_id", userId)

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

}