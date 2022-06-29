package com.mdq.social.ui.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.help.HelpViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityHelpBinding
import com.mdq.social.ui.setting.SettingActivity


class HelpActivity: BaseActivity<ActivityHelpBinding, HelpNavigator>(),
    HelpNavigator {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, HelpActivity::class.java)
        }
    }

    private var activityHelpBinding: ActivityHelpBinding? = null
    private var helpViewModel: HelpViewModel? = null
    private var name:String?=null
    private var mobile:String?=null
    private var email:String?=null
    private var message:String?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_help
    }

    override fun getViewBindingVarible(): Int {
        return BR.helpViewModel
    }

    override fun getViewModel(): BaseViewModel<HelpNavigator> {
        helpViewModel =
            ViewModelProvider(this, viewModelFactory).get(HelpViewModel::class.java)
        return helpViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHelpBinding = getViewDataBinding()
        activityHelpBinding?.helpViewModel = helpViewModel
        helpViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        getProfileDetail()

        activityHelpBinding?.send?.setOnClickListener {
            name=activityHelpBinding!!.YourNameE.text.toString()
            mobile=activityHelpBinding!!.YourNoE.text.toString()
            email=activityHelpBinding!!.YourEmailE.text.toString()
            message=activityHelpBinding!!.message.text.toString()
            if(!name.isNullOrEmpty() && !mobile.isNullOrEmpty() && !email.isNullOrEmpty() && !message.isNullOrEmpty()){
                sendMail()
            }

        }

    }

    private fun getProfileDetail() {

        helpViewModel!!.getUserProfileDetails(
            appPreference.USERID
        ).observe(this,{
                response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileDetailResponse
                if(userProfileResponse!=null){

                    activityHelpBinding?.YourNameE?.setText(userProfileResponse.data?.get(0)?.user_name)
                    activityHelpBinding?.YourNoE?.setText(userProfileResponse.data?.get(0)?.mobile)
                    activityHelpBinding?.YourEmailE?.setText(userProfileResponse.data?.get(0)?.email)

                }
            }
        })

    }

    private fun sendMail() {

        helpViewModel?.HelpSupport(name!!,mobile!!,email!!,message!!)
            ?.observe(this, Observer { response ->
                if (response?.data != null) {
                    var SignupResponses = response.data as SignupResponse
                    if (SignupResponses .message.equals("Contact details sent successfully!")) {
                        showToast("Your query has been raised successfully!!")
                        activityHelpBinding?.message?.setText("")
                    } else {
                        showToast(SignupResponses.message!!)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    override fun onClick() {
        startActivity(SettingActivity.getCallingIntent(this))
    }
}