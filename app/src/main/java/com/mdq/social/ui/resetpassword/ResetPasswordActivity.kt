package com.mdq.social.ui.resetpassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.forgotpassword.ForgotPasswordResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.forgot.ForgotPasswordViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityResetPasswordBinding
import com.mdq.social.ui.forgot.ForgotPasswordNavigator
import com.mdq.social.ui.login.LoginActivity

class ResetPasswordActivity: BaseActivity<ActivityResetPasswordBinding, ForgotPasswordNavigator>(),
    ForgotPasswordNavigator {

    companion object {
        fun getCallingIntent(context: Context,mobile: String,otp:String): Intent {
            return Intent(context, ResetPasswordActivity::class.java)
                .putExtra(AppConstants.MOBILENO,mobile)
                .putExtra(AppConstants.OTP,otp)
    }
    }

    private var activityResetPasswordBinding: ActivityResetPasswordBinding? = null
    private var forgotPasswordViewModel: ForgotPasswordViewModel? = null
    private var mobileNo:String?=null
    private var otp:String?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_reset_password
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

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        activityResetPasswordBinding = getViewDataBinding()
        activityResetPasswordBinding?.forgotPasswordViewModel = forgotPasswordViewModel
        forgotPasswordViewModel?.navigator = this
        if (intent.extras != null) {
            mobileNo = intent!!.extras!!.getString(AppConstants.MOBILENO)
        }
        forgotPasswordViewModel!!.mobile.set(mobileNo)
        forgotPasswordViewModel!!.otp.set(otp)

        activityResetPasswordBinding!!.imageView.setOnClickListener {
            onBackPressed()
        }

    }

    private fun forgot(status:String) {
        forgotPasswordViewModel!!.getForgot().observe(this, { response ->
            if (response?.data != null) {
                var forgotPasswordResponse = response.data as ForgotPasswordResponse
                if (forgotPasswordResponse.message .equals("password updated successfully!")) {
                    startActivity(LoginActivity.getCallingIntent(this))
                    showToast("Password Changed Successfully!!")
                }else {
                    showToast(forgotPasswordResponse.message!!)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun loginVerify(status: Int) {
        if (status==3){
            if (!forgotPasswordViewModel?.password?.get().toString().equals(forgotPasswordViewModel?.createPassword?.get().toString())) {
                showToast(getString(R.string.Password_does_not_match))
                return
            }
            forgot(status.toString())
        }
    }
}