package com.mdq.social.ui.splash

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.splash.SplashViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySplashBinding
import com.mdq.social.ui.BioMetrix.BioMetrix
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.login.LoginActivity

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashNavigator>(), SplashNavigator {
    private var activitySplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashViewModel? = null
    private var dialog: Dialog? = null
    var preferenceManager: PreferenceManager?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewBindingVarible(): Int {
        return BR.splashViewModel
    }

    override fun getViewModel(): BaseViewModel<SplashNavigator> {
        splashViewModel =
            ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)
        return splashViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = getViewDataBinding()
        activitySplashBinding?.splashViewModel = splashViewModel
        splashViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        Handler(Looper.getMainLooper()).postDelayed({
            if (appPreference.USERID.isNullOrEmpty()) {
                startActivity(LoginActivity.getCallingIntent(this))
                finish()
            } else {
                if(getPreferenceManager()?.getSwitches()==true){
                    startActivity(Intent(this@SplashActivity,BioMetrix::class.java))
                    finish()
                }else {
                    startActivity(HomeActivity.getCallingIntent(this))
                    finish()
                }
            }
        }, 3000)
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()!!
            preferenceManager?.initialize(applicationContext)
        }
        return preferenceManager
    }
}