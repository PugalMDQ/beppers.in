package com.mdq.social.ui.forgot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.forgot.ForgotPasswordViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityForgotBinding

class ForgotPasswordActivity : BaseActivity<ActivityForgotBinding, ForgotPasswordNavigator>(),
    ForgotPasswordNavigator {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ForgotPasswordActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private var activityForgotBinding: ActivityForgotBinding? = null
    private var forgotPasswordViewModel: ForgotPasswordViewModel? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_forgot
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

        activityForgotBinding = getViewDataBinding()
        activityForgotBinding?.forgotPasswordViewModel = forgotPasswordViewModel
        forgotPasswordViewModel?.navigator = this

        activityForgotBinding!!.imageView.setOnClickListener {
            onBackPressed()
        }

    }

    override fun loginVerify(status: Int) {

    }
}
