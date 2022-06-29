package com.mdq.social.ui.privacy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.privacy.PrivacyDetail
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.privacy.PrivacyViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityPrivacyBinding
import com.mdq.social.ui.blockaccount.BlockAccountActivity
import com.mdq.social.ui.blockcomment.BlockCommentActivity
import com.mdq.social.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_privacy.*

class PrivacyActivity :BaseActivity<ActivityPrivacyBinding, PrivacyNavigator>(),
    PrivacyNavigator {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, PrivacyActivity::class.java)
        }
    }

    private var activityPrivacyBinding: ActivityPrivacyBinding? = null
    private var privacyViewModel: PrivacyViewModel? = null
    var ii:String?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_privacy
    }

    override fun getViewModel(): BaseViewModel<PrivacyNavigator> {
        privacyViewModel =
            ViewModelProvider(this, viewModelFactory).get(PrivacyViewModel::class.java)
        return privacyViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.privacyViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        activityPrivacyBinding = getViewDataBinding()
        activityPrivacyBinding?.privacyViewModel = privacyViewModel
        privacyViewModel?.navigator = this

        imageView.setOnClickListener {
            onBackPressed()
        }

       getting()
        if(appPreference.TYPE.equals("user")){
            activityPrivacyBinding?.textView153?.visibility= View.VISIBLE
            activityPrivacyBinding?.descForComments?.visibility= View.VISIBLE
            activityPrivacyBinding?.textView154?.visibility= View.VISIBLE
            activityPrivacyBinding?.descForAccounts?.visibility= View.VISIBLE
        }

    }
    private fun getting() {
        privacyViewModel!!.getPrivacy(
        ).observe(this@PrivacyActivity, Observer { response ->
            if(response.data!=null) {
                val privacydetail = response.data as PrivacyDetail
                if(privacydetail.data!=null){
                    if (privacydetail.data!!.get(0).privacy!!.equals("1")) {
                        activityPrivacyBinding?.switch6?.isChecked=true
                        ii="1"
                    }else{
                        activityPrivacyBinding?.switch6?.isChecked=false
                        ii="0"
                    }
                }else{
                    activityPrivacyBinding?.switch6?.isChecked=false
                    ii="0"
                }
            }
        })



    }

    override fun onClick(status: Int) {
        if (status==1){

        }else if (status==2){
            if(activityPrivacyBinding?.switch6?.isChecked == true){
                ii="1"
                privacyCall()

            }else{
                ii="0"
                privacyCall()
            }
        }else if (status==3){
            startActivity(BlockCommentActivity.getCallingIntent(this))
        }else if (status==4){
            startActivity(BlockAccountActivity.getCallingIntent(this))
        }else if (status==5){
            deactivateFun();
        }
    }

    private fun deactivateFun() {
        privacyViewModel?.deactivate()
            ?.observe(this, Observer { response ->
                if (response?.data != null) {
                    var SignupResponses = response.data as SignupResponse
                    if (SignupResponses .message.equals("Your Account will be deactivated soon")) {

                        showToast(SignupResponses.message!!)
                        appPreference.clearAppPreference()
                        startActivity(LoginActivity.getCallingIntent(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        finish()
                        
                    } else {
                        showToast(SignupResponses.message!!)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    private fun privacyCall() {
        privacyViewModel?.privacyUpdate(appPreference.USERID,ii!!)
            ?.observe(this, Observer { response ->
                if (response?.data != null) {
                    var SignupResponses = response.data as SignupResponse
                    if (SignupResponses .message.equals("Privacy request sent successfully!")) {
                        if(appPreference.PRIVACY.equals("0")) {
                            appPreference.PRIVACY = "1"

                        }else if (appPreference.PRIVACY.equals("1")){
                            appPreference.PRIVACY = "0"

                        }
                    } else {
                        showToast(SignupResponses.message!!)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

}