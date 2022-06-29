package com.mdq.social.ui.setting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.setting.SettingViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySettingBinding
import com.mdq.social.ui.Security.Security
import com.mdq.social.ui.businessupdate.BusinessUpdateActivity
import com.mdq.social.ui.follow.FollowActivity
import com.mdq.social.ui.freelanceupdate.FreelanceUpdateActivity
import com.mdq.social.ui.help.HelpActivity
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.individual.IndividualActivity
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.ui.notificationsetting.NotificationSettingActivity
import com.mdq.social.ui.pendingrequest.PendingRequestActivity
import com.mdq.social.ui.privacy.PrivacyActivity
import kotlinx.android.synthetic.main.dialog_logout.*
import javax.inject.Inject

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingNaviagtor>(), SettingNaviagtor {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SettingActivity::class.java)
        }
    }

    @Inject
    override lateinit var appPreference: AppPreference

    private var activitySettingBinding: ActivitySettingBinding? = null
    private var settingViewModel: SettingViewModel? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun getViewBindingVarible(): Int {
        return BR.settingViewModel
    }

    override fun getViewModel(): BaseViewModel<SettingNaviagtor> {
        settingViewModel =
            ViewModelProvider(this, viewModelFactory).get(SettingViewModel::class.java)
        return settingViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingBinding = getViewDataBinding()
        activitySettingBinding?.settingViewModel = settingViewModel
        settingViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        if(appPreference.TYPE.equals("business") || appPreference.TYPE.equals("freelancer") ){
            activitySettingBinding?.textView43?.visibility= View.GONE
            activitySettingBinding?.imageView14?.visibility= View.GONE
            activitySettingBinding?.imageView12?.visibility= View.GONE
            activitySettingBinding?.textView39?.visibility= View.GONE
        }
    }

    override fun onClick(state: Int) {
        if (state==1){
            startActivity(Intent(this@SettingActivity,HomeActivity::class.java))
        }else if (state==2) {
            if (appPreference.USERGROUP.equals("business")) {
                startActivity(BusinessUpdateActivity.getCallingIntent(this))
            } else if (appPreference.USERGROUP.equals("user")) {
                startActivity(IndividualActivity.getCallingIntent(this))
            } else if (appPreference.USERGROUP.equals("freelance")) {
                startActivity(FreelanceUpdateActivity.getCallingIntent(this))
            }
        }
        else if (state==3){
            startActivity(FollowActivity.getCallingIntent(this))
        }else if (state==4){
            startActivity(PrivacyActivity.getCallingIntent(this))
        }else if (state==5){
            startActivity(Intent(this,Security::class.java))
        }else if (state==6){
            startActivity(PendingRequestActivity.getCallingIntent(this))
        }else if (state==7){
            startActivity(NotificationSettingActivity.getCallingIntent(this))
        }else if (state==8){
            startActivity(HelpActivity.getCallingIntent(this))
        }else if (state==9){
            logout()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun logout() {
        val dialoglogout = Dialog(this, R.style.dialog_center)
        dialoglogout.setCancelable(false)
        dialoglogout.setContentView(com.mdq.social.R.layout.dialog_logout)
        dialoglogout.show()
        val textView23 = dialoglogout.textView23
        val textView24 = dialoglogout.textView24
        textView23.setOnClickListener {
            dialoglogout.dismiss()
        }
        textView24.setOnClickListener {
            dialoglogout.dismiss()
            appPreference.clearAppPreference()
            startActivity(LoginActivity.getCallingIntent(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }
    }

}