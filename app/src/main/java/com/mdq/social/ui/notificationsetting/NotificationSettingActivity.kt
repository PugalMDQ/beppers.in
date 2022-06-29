package com.mdq.social.ui.notificationsetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.NotificationSetting.NotificationSettingResponse
import com.mdq.social.app.data.response.recent.RecentResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.notificationsetting.NotificationSettingViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityNotificationSettingBinding
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.ui.setting.SettingActivity
import kotlinx.android.synthetic.main.activity_notification_setting.*
import kotlinx.android.synthetic.main.fragment_home.*

class NotificationSettingActivity: BaseActivity<ActivityNotificationSettingBinding, NotificationSettingNavigator>(),
    NotificationSettingNavigator {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, NotificationSettingActivity::class.java)
        }
    }

    private var activityNotificationSettingBinding: ActivityNotificationSettingBinding? = null
    private var notificationSettingViewModel: NotificationSettingViewModel? = null
    private var likecomment:String?=null
    private var message:String?=null
    private var followRequest:String?=null
    private var folloeAcceptance:String?=null
    private var user_post:String?=null
    override fun getLayoutId(): Int {
        return R.layout.activity_notification_setting
    }

    override fun getViewModel(): BaseViewModel<NotificationSettingNavigator> {
        notificationSettingViewModel =
            ViewModelProvider(this, viewModelFactory).get(NotificationSettingViewModel::class.java)
        return notificationSettingViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.notificationSettingViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNotificationSettingBinding = getViewDataBinding()
        activityNotificationSettingBinding?.notificationSettingViewModel = notificationSettingViewModel
        notificationSettingViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        getNotificationDetail()

    }

    private fun getNotificationDetail() {

        notificationSettingViewModel!!.getNotificationSetting()
            .observe(this@NotificationSettingActivity, Observer { response ->
                if (response?.data != null) {
                    val response = response.data as NotificationSettingResponse
                    if(response.data!=null){
                        if(response.data.get(0).like_comment.equals("1")){
                            activityNotificationSettingBinding?.switch1?.isChecked=true
                            likecomment="1"
                            appPreference.LIKEANDCOMMENT="1"

                        }else{
                            activityNotificationSettingBinding?.switch1?.isChecked=false
                            likecomment="0"
                            appPreference.LIKEANDCOMMENT="0"

                        }
                        if(response.data.get(0).message.equals("1")){
                            activityNotificationSettingBinding?.switch2?.isChecked=true
                            message="1"
                            appPreference.MESSAGE="1"
                        }else{
                            activityNotificationSettingBinding?.switch2?.isChecked=false
                            message="0"
                            appPreference.MESSAGE="0"
                        }
                        if(response.data.get(0).follow_request.equals("1")){
                            activityNotificationSettingBinding?.switch4?.isChecked=true
                            followRequest="1"
                            appPreference.FOLLOWERREQUEST="1"
                        } else{
                            activityNotificationSettingBinding?.switch4?.isChecked=false
                            followRequest="0"
                            appPreference.FOLLOWERREQUEST="0"
                        }
                        if(response.data.get(0).follow_accept.equals("1")){
                            activityNotificationSettingBinding?.switch5?.isChecked=true
                            folloeAcceptance="1"
                            appPreference.FOLLOWERACCEPTANCE="1"
                        }else{
                            activityNotificationSettingBinding?.switch5?.isChecked=false
                            folloeAcceptance="0"
                            appPreference.FOLLOWERACCEPTANCE="0"
                        }
                        if(response.data.get(0).user_post.equals("1")){
                            activityNotificationSettingBinding?.switch3?.isChecked=true
                            user_post="1"
                            appPreference.POSTS="1"
                        }else{
                            activityNotificationSettingBinding?.switch3?.isChecked=false
                            user_post="0"
                            appPreference.POSTS="0"
                        }
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    override fun onClick(state: Int) {
        if (state==1){
            startActivity(SettingActivity.getCallingIntent(this))
        }else if (state==2){
            if(switch1.isChecked){
                likecomment="1"
                appPreference.LIKEANDCOMMENT="1"
                postNotification()
               }else{
                likecomment="0"
                appPreference.LIKEANDCOMMENT="0"
                postNotification()
               }

        }else if (state==3){
            if(switch2.isChecked){
                message="1"
                appPreference.MESSAGE="1"
                postNotification()
            }else{
                message="0"
                appPreference.MESSAGE="0"
                postNotification()
            }

        }else if (state==4){
            if(switch3.isChecked){
                user_post="1"
                appPreference.POSTS="1"
                postNotification()
            }else{
                user_post="0"
                appPreference.POSTS="0"
                postNotification()
            }

        }else if (state==5){
            if(switch4.isChecked){
                followRequest="1"
                appPreference.FOLLOWERREQUEST="1"
                postNotification()
            }else{
                followRequest="0"
                appPreference.FOLLOWERREQUEST="0"
                postNotification()
            }

        }else if (state==6){
            if(switch5.isChecked){
                folloeAcceptance="1"
                appPreference.FOLLOWERACCEPTANCE="1"
                postNotification()
            }else{
                folloeAcceptance="0"
                appPreference.FOLLOWERACCEPTANCE="0"
                postNotification()
            }
        }
    }

    private fun postNotification() {
        notificationSettingViewModel!!.setNotificationSettings(appPreference.USERID,likecomment!!,message!!,followRequest!!,folloeAcceptance!!,user_post!!).observe(this,
            { response ->
                if (response?.data != null) {
                    var signupResponse = response.data as SignupResponse
                    if (signupResponse!!.message.equals("Failed to fetch Notification On/off details")) {
                    } else {
                        showToast(signupResponse!!.message.toString())
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }
    }
