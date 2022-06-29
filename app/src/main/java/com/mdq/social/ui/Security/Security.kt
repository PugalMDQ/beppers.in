package com.mdq.social.ui.Security

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.mdq.social.PreferenceManager
import com.mdq.social.databinding.ActivitySecurity2Binding
import com.mdq.social.ui.resetpassword.MoblieNumAdOtp

class Security : AppCompatActivity() {

    private var activitySettingBinding: ActivitySecurity2Binding? = null
    var preferenceManager:PreferenceManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingBinding = ActivitySecurity2Binding.inflate(layoutInflater)
        val view: View = activitySettingBinding!!.getRoot()
        setContentView(view)
//
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        if(getPreferenceManager()?.getSwitches()==true){
            activitySettingBinding?.switchBio?.isChecked=true
        }

        activitySettingBinding?.imageView?.setOnClickListener {
            onBackPressed()
        }
        activitySettingBinding?.switchBio?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                getPreferenceManager()?.setSwitchBio(true)
            }else{
                getPreferenceManager()?.setSwitchBio(false)
            }
        }

        activitySettingBinding?.REset?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(this@Security,MoblieNumAdOtp::class.java))
            }
        })
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