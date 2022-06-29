package com.mdq.social.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.ui.business.BusinessProfileFragment
import com.mdq.social.ui.freelancer.FreelancerFragment

class ProfileActivity : AppCompatActivity() {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
    lateinit var appPreference: AppPreference
    var preferenceManager: PreferenceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        appPreference = AppPreference(this)

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        val intent = intent.extras
        var ii = intent?.getString("id", null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val background = resources.getDrawable(R.drawable.bg_gradiant)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }

        if (appPreference.USERGROUP == "2") {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<FreelancerFragment>(R.id.fragment_container_view)
            }
        } else if (appPreference.USERGROUP == "1") {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ProfileFragment>(R.id.fragment_container_view)
            }
        } else

        {

            var fragmentManager: FragmentManager? = null
            var fragmentTransaction: FragmentTransaction? = null
            fragmentManager=supportFragmentManager

            val se = BusinessProfileFragment()
            val bundle1 = Bundle()
            bundle1.putString("message", ii)
            se.setArguments(bundle1)
            fragmentTransaction = fragmentManager?.beginTransaction()?.add(R.id.fragment_container_view, se, null)
            fragmentTransaction?.commit()


        }

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