package com.mdq.social.ui.pendingrequest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.connectionrequest.ConnectionRequest
import com.mdq.social.app.data.response.followresponse.FollowerPendingItem
import com.mdq.social.app.data.response.followstatusupdate.FollowStatusUpdateResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.pendingrequest.PendingRequestViewModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityPendingRequestBinding
import com.mdq.social.ui.Firebase.Constants
import com.mdq.social.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_pending_request.*
import org.json.JSONObject
import java.util.HashMap

class PendingRequestActivity : BaseActivity<ActivityPendingRequestBinding, PendingRequestNavigator>(),PendingRequestAdapter.PendingClickManager,
    PendingRequestNavigator,RequestAdapter.clicks {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, PendingRequestActivity::class.java)
        }
    }

    private var activityPendingRequestBinding: ActivityPendingRequestBinding? = null
    private var pendingRequestViewModel: PendingRequestViewModel? = null
    private var pendingRequestAdapter: PendingRequestAdapter? = null
    private var requestAdapter: RequestAdapter? = null
    private var ingnoredAdapter: IgnoredAdapter? = null
    private var followerPendingItem: List<FollowerPendingItem>?=null
    private var tab_position:Int?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_pending_request
    }

    override fun getViewBindingVarible(): Int {
        return BR.pendingRequestViewModel
    }

    override fun getViewModel(): BaseViewModel<PendingRequestNavigator> {
        pendingRequestViewModel =
            ViewModelProvider(this, viewModelFactory).get(PendingRequestViewModel::class.java)
        return pendingRequestViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPendingRequestBinding = getViewDataBinding()
        activityPendingRequestBinding?.pendingRequestViewModel = pendingRequestViewModel
        pendingRequestViewModel?.navigator = this

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE);


        tab_position = tb_trip.selectedTabPosition

        tb_trip.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab_position = tab.position
                getFollowFollowingList(appPreference.USERID)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        getFollowFollowingList(appPreference.USERID)

    }

    private fun getFollowFollowingList(userId: String) {

        if(tab_position==0) {
            pendingRequestViewModel!!.getfollowFollowingList(appPreference.USERID)
                .observe(this, Observer { response ->
                    activityPendingRequestBinding?.rvPending?.visibility=View.VISIBLE
                    activityPendingRequestBinding?.rvIgnored?.visibility=View.GONE
                    if (response?.data != null) {
                        val followResponse = response.data as ConnectionRequest
                        if(followResponse.data!=null) {
                            requestAdapter = RequestAdapter(this, followResponse, this);
                            activityPendingRequestBinding?.rvPending?.adapter = requestAdapter
                        }else{
                            requestAdapter = RequestAdapter(this, followResponse, this);
                            activityPendingRequestBinding?.rvPending?.adapter = requestAdapter
                        }
                    } else {
                        showToast(response.throwable?.message!!)
                    }
                })
        }
        else{
            pendingRequestViewModel!!.getIgnoreList(appPreference.USERID)
                .observe(this, Observer { response ->
                    if (response?.data != null) {
                        activityPendingRequestBinding?.rvPending?.visibility=View.GONE
                        activityPendingRequestBinding?.rvIgnored?.visibility=View.VISIBLE
                        val followResponse = response.data as ConnectionRequest
                        if(followResponse.data!=null) {
                            ingnoredAdapter = IgnoredAdapter(this, followResponse)
                            activityPendingRequestBinding?.rvIgnored?.adapter = ingnoredAdapter
                        }else{
                            ingnoredAdapter = IgnoredAdapter(this, followResponse)
                            activityPendingRequestBinding?.rvIgnored?.adapter = ingnoredAdapter
                        }
                    } else {
                        showToast(response.throwable?.message!!)
                    }
                })
        }
    }

    override fun onClick(state: Int) {
        if (state==1){
            startActivity(SettingActivity.getCallingIntent(this))
        }
    }

    override fun onAcceptClick(followerId: Int) {
        getFollow(appPreference.USERID,followerId.toString(),"1")
    }

    override fun onDeclineClick(followerId: Int) {
        getFollow(appPreference.USERID,followerId.toString(),"3")
    }

    private fun getFollow(userId:String,followerId:String,followStatus:String) {
        pendingRequestViewModel!!.getFollowStatus(userId,followerId,followStatus).observe(this, Observer { response ->
            if (response?.data != null) {
                val followStatusUpdateResponse = response.data as FollowStatusUpdateResponse

                if (followStatusUpdateResponse.status.equals("success")) {
                    getFollowFollowingList(appPreference.USERID)

                } else {
                    showToast(followStatusUpdateResponse.message!!)
                }

            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onAcceptClick(position: Int, followerID: String,username:String) {

        pendingRequestViewModel!!.PostAccept(appPreference.USERID,followerID).observe(this, Observer { response ->
            if (response?.data != null) {
                val followStatusUpdateResponse = response.data as SignupResponse

                if (followStatusUpdateResponse.message.equals("Follow request Accept successfully!")) {
                    getFollowFollowingList(appPreference.USERID)
                    prepareNotificationMessage(followerID,appPreference.USERID,appPreference.USERNAME)
                    insertNotification("",followerID,appPreference.USERNAME)
                } else {
                    showToast(followStatusUpdateResponse.message!!)
                }

            } else {
                showToast(response.throwable?.message!!)
            }
        })

    }

    override fun onIgnoreClick(position: Int, followerID: String) {
        pendingRequestViewModel!!.PostIgnore(appPreference.USERID,followerID).observe(this, Observer { response ->
            if (response?.data != null) {
                val followStatusUpdateResponse = response.data as SignupResponse

                if (followStatusUpdateResponse.message.equals("Follow request Ignored successfully!")) {
                    getFollowFollowingList(appPreference.USERID)
                } else {
                    showToast(followStatusUpdateResponse.message!!)
                }

            } else {
                showToast(response.throwable?.message!!)
            }
        })

    }

    private fun prepareNotificationMessage(to:String,from: String, name: String) {

        val NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC
        val NOTIFICATION_TITLE = name
        val NOTIFICATION_MESSAGE = "has accept your follow request!!"
        val NOTIFICATION_TYPE = "notificationType_Accept"

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

        } catch (e: java.lang.Exception) {

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
        pendingRequestViewModel?.insertNotification(postId,toID,name)?.observe(this, Observer { response ->
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