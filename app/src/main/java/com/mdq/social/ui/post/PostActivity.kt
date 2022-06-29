package com.mdq.social.ui.post

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdq.social.BR
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.CommentResponse.CommentResponse
import com.mdq.social.app.data.response.addcomment.AddCommentResponse
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.post.PostViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityPostBinding
import com.mdq.social.ui.Comments.AdapterForComment
import com.mdq.social.ui.listOfPost.AdapterForList
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.layout_commnets.*
import android.widget.AbsListView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.mdq.social.R
import com.mdq.social.app.data.response.CommentResponseForblock.CommentresponseforBlock
import com.mdq.social.app.data.response.CommentResponseForblock.DataItem
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.ui.Firebase.Constants
import kotlinx.android.synthetic.main.activity_live_chat.*
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class PostActivity : BaseActivity < ActivityPostBinding, PostNavigator>(), PostNavigator,

    PostAdapter.ReplyClickManager{
    companion object {
        fun getCallingIntent(context: Context, albumId: String, path: String,user_id:String,name:String): Intent {
            return Intent(context, PostActivity::class.java).putExtra(AppConstants.ALBUM_ID, albumId).putExtra(AppConstants.PATH, path).putExtra("user_id",user_id).putExtra("name",name)
        }
    }
    private var postViewModel: PostViewModel? = null
    private var activityPostBinding: ActivityPostBinding? = null
    private var albumId: String? =""
    private var User_id: String? = ""
    private var name: String? = ""
    private var path: String? = ""
    private var from: String? = ""
    private var TOUSer: String? = ""
    private var position: Int? =null
    private var postAdapter: PostAdapter? = null
    private var adapter: AdapterForComment? = null
    var linearLayoutManager:LinearLayoutManager?=null
    var adapterForList: AdapterForList?=null
    var blockedID: List<DataItem?>?=null
    override fun getLayoutId(): Int {
        return R.layout.activity_post
    }

    override fun getViewBindingVarible(): Int {
        return BR.postViewModel
    }

    override fun getViewModel(): BaseViewModel<PostNavigator> {
        postViewModel =
            ViewModelProvider(this, viewModelFactory).get(PostViewModel::class.java)
        return postViewModel!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPostBinding = getViewDataBinding()
        activityPostBinding?.postViewModel = postViewModel
        linearLayoutManager= LinearLayoutManager(this)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        if (intent.extras != null) {
            albumId = intent!!.extras!!.getString(AppConstants.ALBUM_ID).toString()
            path = intent!!.extras!!.getString(AppConstants.PATH)
            User_id= intent!!.extras!!.getString("user_id").toString()
            from= intent!!.extras!!.getString("ProfileAdapter").toString()
            position= intent!!.extras!!.getInt("position")
            name= intent!!.extras!!.getString("name")
        }

        getBlockedList()

        if(from!!.equals("ProfileAdapter")){
            activityPostBinding?.consAddComment?.visibility=View.GONE
            activityPostBinding?.title?.setText("Post")
            getProfileDetail(User_id!!)
        }else{
            getCommentDetails(albumId.toString())
        }
        activityPostBinding!!.back.setOnClickListener {
            onBackPressed()
        }

            activityPostBinding?.tvPost?.setOnClickListener {
                if (TextUtils.isEmpty(activityPostBinding?.editText?.text.toString())) {
                    showToast(getString(R.string.please_enter_comment))
                    return@setOnClickListener
                }

                postViewModel!!.getAddComment(
                    albumId.toString(),
                    appPreference.USERID,
                    activityPostBinding?.editText?.text.toString()
                ).observe(this, Observer { response ->
                    if (response?.data != null) {
                        val addCommentResponse = response.data as AddCommentResponse
                        activityPostBinding?.editText?.setText("")
                        if (addCommentResponse != null) {
                            if (addCommentResponse.message?.equals("Comments added successfully!")!!) {
                                getCommentDetails(albumId.toString())
                                prepareNotificationMessage(User_id!!,appPreference.USERID,appPreference.USERNAME!!)
                                insertNotification(albumId.toString(),User_id!!,appPreference.USERNAME!!)
                            } else {
                                showToast(addCommentResponse.message!!)
                            }
                        } else {
                            showToast("Something went wrong")
                        }
                    } else {
                        showToast(response.throwable?.message!!)
                    }
                })
            }
    }

    private fun getBlockedList() {
        postViewModel!!.getCommentBlockList(User_id!!).observe(this, Observer { response ->
            if (response?.data != null) {
                val commentBlockLists = response.data as CommentresponseforBlock
                if (commentBlockLists != null) {
                    blockedID=commentBlockLists.data!!
                    filter()
                } else {
                    showToast(commentBlockLists.message!!)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun filter() {
            for (i in blockedID!!.indices) {
            if ( User_id?.trim().equals(blockedID!!.get(i)?.user_id) && blockedID!!.get(i)?.block_user_id?.trim().equals(appPreference.USERID.trim())) {
                    activityPostBinding!!.consAddComment.visibility = View.GONE
                }
            }
    }

    private fun getProfileDetail(user_id: String) {

            postViewModel!!.getUserProfile(
                user_id
            ).observe(this, Observer { response ->
                if (response?.data != null) {

                    val UserProfileResponses = response.data as UserProfileResponse
                    if(UserProfileResponses!=null) {

                        activityPostBinding?.rv!!.setRecyclerListener(object : AbsListView.RecyclerListener,
                            RecyclerView.RecyclerListener {
                            override fun onMovedToScrapHeap(view: View) {
                                var videoview:VideoView=view.findViewById(R.id.payer1)
                                videoview.stopPlayback()
                                videoview.seekTo(1)
                            }
                            override fun onViewRecycled(holder: RecyclerView.ViewHolder){

                            }
                        })
                    }
                }
            })
    }

    private fun getCommentDetails(albumId: String) {
        postViewModel!!.getCommentDetails(albumId).observe(this, Observer { response ->
            if (response?.data != null) {
                val commentDetailsResponse = response.data as CommentResponse
                if (commentDetailsResponse != null) {
                    if (commentDetailsResponse.data != null) {
                        activityPostBinding!!.textView50.visibility=View.INVISIBLE
                        activityPostBinding!!.imageView58.visibility=View.INVISIBLE

                        adapter= AdapterForComment(this,commentDetailsResponse)
                        activityPostBinding?.rv?.adapter=adapter
                        activityPostBinding?.rv?.layoutManager=linearLayoutManager
                        adapter!!.notifyItemChanged(position!!)
                    } else {
                        activityPostBinding!!.textView50.visibility=View.VISIBLE
                        activityPostBinding!!.imageView58.visibility=View.VISIBLE
                    }
                } else {
                    showToast(commentDetailsResponse.message!!)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onItemReplyClick(commentsId: Int, etComment: EditText, post: ConstraintLayout) {
        if (TextUtils.isEmpty(etComment.text.toString())) {
            showToast(getString(R.string.please_enter_comment))
            return
        }

        postViewModel!!.getSubAddComment(
            albumId.toString(),
            appPreference.USERID,
            commentsId.toString(),
            etComment.text.toString()

        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val addCommentResponse = response.data as AddCommentResponse

                activityPostBinding?.editText?.setText("")
                if (addCommentResponse != null) {
                    if (addCommentResponse.message?.equals("success")!!) {
                        post.visibility = View.GONE
                        getCommentDetails(albumId.toString())
                    } else {
                        showToast(addCommentResponse.message!!)
                    }
                } else {
                    showToast("Something went wrong")
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun prepareNotificationMessage(to:String,from: String, name: String) {

        val NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC
        val NOTIFICATION_TITLE = name
        val NOTIFICATION_MESSAGE = "has comment your post!!"
        val NOTIFICATION_TYPE = "notificationType_Comment"

        //prepare json
        val notificationJo = JSONObject()
        val notificationBodyJo = JSONObject()

        try {

            //what to send
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE)
            notificationBodyJo.put("to_id", to)
            notificationBodyJo.put("from_id", from)
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE)
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE)

            //where to send
            notificationJo.put("to", NOTIFICATION_TOPIC) //to all who subscribed
            notificationJo.put("data", notificationBodyJo)

        } catch (e: Exception) {
            e.message?.let { showToast(it) }
        }
        sendFcmNotification(notificationJo)
        subscribeTOTopic(to,from,NOTIFICATION_TOPIC)
    }

    private fun subscribeTOTopic(buyerId: String, sellerId: String, notificationTopic: String) {

        Firebase.messaging.subscribeToTopic(Constants.FCM_TOPIC)
            .addOnCompleteListener { task ->
                var msg = "R.string.msg_subscribed"
                if (!task.isSuccessful) {
                    msg =" R.string.msg_subscribe_failed"
                }
                Log.d(AppSignatureHelper.TAG, msg)
            }
    }

    private fun sendFcmNotification(notificationJo: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo,
                Response.Listener { response: JSONObject? -> },
                Response.ErrorListener {
                    Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    //put request headers
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "key=" + Constants.FCM_KEY
                    return headers
                }
            }
        //enque the volley request
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }
    private fun insertNotification(postId: String, toID: String,name:String) {
        postViewModel?.insertNotification(postId,toID,name)?.observe(this, Observer { response ->
            if (response?.data != null) {
                val res = response.data as SignupResponse
                if (res.message.equals("Notification added successfully")) {

                }else{
                    res.message?.let { showToast(it) }
                }
            }
        })
    }



}