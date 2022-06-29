package com.mdq.social.ui.signupselection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.signupselection.SignupSelectionModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySignupTypeSelectionBinding
import com.mdq.social.ui.signup.SignUpActivity
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import com.mdq.social.ui.signupfreelancer.SignUpFreelancerActivity

class SignUpSelectionActivity :
    BaseActivity<ActivitySignupTypeSelectionBinding, SignupSelectionNavigator>(),
    SignupSelectionNavigator {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpSelectionActivity::class.java)

        }
    }

    var preferenceManager:PreferenceManager?=null
    private var activitySignUpSelectionBinding: ActivitySignupTypeSelectionBinding? = null
    private var signupSelectionModel: SignupSelectionModel? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_signup_type_selection
    }

    override fun getViewBindingVarible(): Int {
        return BR.loginviewmodel
    }

    override fun getViewModel(): BaseViewModel<SignupSelectionNavigator> {
        signupSelectionModel =
            ViewModelProvider(this, viewModelFactory).get(SignupSelectionModel::class.java)
        return signupSelectionModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpSelectionBinding = getViewDataBinding()
        activitySignUpSelectionBinding?.signupselectionviewmodel = signupSelectionModel
        signupSelectionModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);
    }

    override fun onBackClick() {
        finish()
    }

    override fun onIndividualClick() {

        startActivity(SignUpActivity.getCallingIntent(this))
        getPreferenceManager()?.setPrefProfile("INDIVIDUAL")
    }

    override fun onBusinessClick() {
        startActivity(SignUpBusinessActivity.getCallingIntent(this))
        getPreferenceManager()?.setPrefProfile("BUSINESS")

    }

    override fun onFreelancerClick() {
        startActivity(SignUpFreelancerActivity.getCallingIntent(this))
        getPreferenceManager()?.setPrefProfile("FREELANCER")
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(applicationContext)
        }
        return preferenceManager
    }
}