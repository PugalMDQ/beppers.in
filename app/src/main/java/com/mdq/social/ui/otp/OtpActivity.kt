package com.mdq.social.ui.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.forgot.ForgotPasswordViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityOtpBinding
import com.mdq.social.ui.forgot.ForgotPasswordNavigator
import com.mdq.social.ui.resetpassword.ResetPasswordActivity

class OtpActivity : BaseActivity<ActivityOtpBinding, ForgotPasswordNavigator>(),
    ForgotPasswordNavigator {

    companion object {
        fun getCallingIntent(context: Context,mobile: String): Intent {
            return Intent(context, OtpActivity::class.java).putExtra(AppConstants.MOBILENO,mobile)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private var activityOtpBinding: ActivityOtpBinding? = null
    private var forgotPasswordViewModel: ForgotPasswordViewModel? = null
    private var mobileNo:String?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_otp
    }

    override fun getViewBindingVarible(): Int {
        return BR.forgotPasswordViewModel
    }

    override fun getViewModel(): BaseViewModel<ForgotPasswordNavigator> {
        forgotPasswordViewModel =
            ViewModelProvider(this, viewModelFactory).get(ForgotPasswordViewModel::class.java)
        return forgotPasswordViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOtpBinding = getViewDataBinding()
        activityOtpBinding?.forgotPasswordViewModel = forgotPasswordViewModel
        forgotPasswordViewModel?.navigator = this
        if (intent.extras != null) {
            mobileNo = intent!!.extras!!.getString(AppConstants.MOBILENO)
        }
        forgotPasswordViewModel!!.mobile.set(mobileNo)
    }


    override fun loginVerify(status:Int) {
       if (status==2){
           startActivity(ResetPasswordActivity.getCallingIntent(this,mobileNo!!,forgotPasswordViewModel!!.otp.get().toString()))
       }else if (status == 5) {
           onBackPressed()
       }
    }
}