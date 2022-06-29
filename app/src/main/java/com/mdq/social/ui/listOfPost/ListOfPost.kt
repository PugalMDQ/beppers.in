package com.mdq.social.ui.listOfPost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.recent.DataItem
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.listOfPost.ListOfPostViewModel
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityListOfPostBinding

class ListOfPost :  BaseActivity < ActivityListOfPostBinding, ListOfPostNavigator>(), ListOfPostNavigator {

    companion object {
        fun getCallingIntent(context: Context, albumId: String, path: String, user_id:String): Intent {
            return Intent(context, ListOfPost::class.java).putExtra(AppConstants.ALBUM_ID, albumId).putExtra(AppConstants.PATH, path).putExtra("user_id",user_id)
        }
    }
    var activityListOfPost:ActivityListOfPostBinding?=null
    var data:List<DataItem>?=null
    var listOfPost: ListOfPostViewModel?=null
    var linearLayoutManager: LinearLayoutManager?=null
    var adapterForList: AdapterForList?=null
    private var albumId: String? =""
    private var User_id: String? = ""
    private var path: String? = ""
    private var from: String? = ""
    private var position: Int? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListOfPost = getViewDataBinding()
        activityListOfPost?.listViewModel = listOfPost
        data=ArrayList<DataItem>()

        if (intent.extras != null) {
            albumId = intent!!.extras!!.getString(AppConstants.ALBUM_ID).toString()
            path = intent!!.extras!!.getString(AppConstants.PATH)
            User_id= intent!!.extras!!.getString("user_id").toString()
            from= intent!!.extras!!.getString("ProfileAdapter").toString()
            position= intent!!.extras!!.getInt("position")
            getProfileDetail(User_id!!)
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_list_of_post
    }

        override fun getViewModel():BaseViewModel<ListOfPostNavigator> {
        listOfPost =
            ViewModelProvider(this, viewModelFactory).get(ListOfPostViewModel::class.java)
        return listOfPost!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.listViewModel
    }

    private fun getProfileDetail(user_id: String) {

        listOfPost!!.getUserProfile(
            user_id
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val UserProfileResponses = response.data as UserProfileResponse
                if(UserProfileResponses!=null) {

                }
            }
        })
    }

}