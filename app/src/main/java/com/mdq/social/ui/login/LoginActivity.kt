package com.mdq.social.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.response.common.ResponseStatus
import com.mdq.social.app.data.response.login.LoginResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.login.LoginViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityLoginBinding
import com.mdq.social.ui.forgot.ForgotPasswordActivity
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.models.User
import com.mdq.social.ui.resetpassword.MoblieNumAdOtp
import com.mdq.social.ui.signupselection.SignUpSelectionActivity
import java.util.ArrayList

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginNavigator>(), LoginNavigator {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private var activitiyLoginBinding: ActivityLoginBinding? = null
    private var loginViewModel: LoginViewModel? = null
    private var token: String = ""
    private var preferenceManager:PreferenceManager ?=null
    var FROMUSERID: String ?=null
    private val notificationList = HashMap<String, User>()


    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewBindingVarible(): Int {
        return BR.loginviewmodel
    }

    override fun getViewModel(): BaseViewModel<LoginNavigator> {
        loginViewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        return loginViewModel!!
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitiyLoginBinding = getViewDataBinding()
        activitiyLoginBinding?.loginviewmodel = loginViewModel
        loginViewModel?.navigator = this

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            val background = resources.getDrawable(R.drawable.bg_gradiant)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }

        CardElevation()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            token = task.result!!
            loginViewModel?.token?.set(token)

        })
        activitiyLoginBinding!!.textView4.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MoblieNumAdOtp::class.java))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun CardElevation() {
        activitiyLoginBinding!!.editTextTextPersonName3.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                        activitiyLoginBinding!!.CardForEditTextTextPersonName3.cardElevation=15F
                        activitiyLoginBinding!!.CardForEditTextTextPersonName4.cardElevation=0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitiyLoginBinding!!.editTextTextPersonName4.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                        activitiyLoginBinding!!.CardForEditTextTextPersonName4.cardElevation=15F
                        activitiyLoginBinding!!.CardForEditTextTextPersonName3.cardElevation=0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })
    }

    override fun loginVerify() {
        login()
    }

    private fun login() {

        activitiyLoginBinding!!.CardForEditTextTextPersonName3.setBackgroundResource(R.drawable.bg_login_back)
        activitiyLoginBinding!!.CardForEditTextTextPersonName4.setBackgroundResource(R.drawable.bg_login_back)

        if (loginViewModel?.username?.get().isNullOrEmpty()) {
            activitiyLoginBinding!!.CardForEditTextTextPersonName3.setBackgroundResource(R.drawable.bg_delete)
            activitiyLoginBinding!!.editTextTextPersonName3.requestFocus()
            showToast(getString(R.string.please_enter_user_name))
            return
        }
        if (loginViewModel?.password?.get().isNullOrEmpty()) {
            activitiyLoginBinding!!.CardForEditTextTextPersonName4.setBackgroundResource(R.drawable.bg_delete)
            activitiyLoginBinding!!.editTextTextPersonName4.requestFocus()
            showToast(getString(R.string.please_enter_password))
            return
        }

        loginViewModel!!.login().observe(this, Observer { response ->
            if (response?.data != null) {
                var loginResponse = response.data as LoginResponse
                if (loginResponse != null && loginResponse.message == ResponseStatus.SUCCESS) {
                    loginViewModel?.intiateLoginPreference(loginResponse)
                    showToast(loginResponse.message!!)
                        fetchUsers()
                    startActivity(HomeActivity.getCallingIntent(this))
                    finish()
                } else {
                    showToast(loginResponse.message!!)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun signup() {
        startActivity(SignUpSelectionActivity.getCallingIntent(this))
    }

    override fun forgotClick() {
        startActivity(ForgotPasswordActivity.getCallingIntent(this))
    }
    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(applicationContext)
        }
        return preferenceManager
    }

    private fun fetchUsers() {
        val users=ArrayList<User>()

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {
                @Suppress("NestedLambdaShadowedImplicitParameter")
                it.getValue(User::class.java)?.let {
                    if (it.name .equals(loginViewModel?.username?.get()) || it.mobileno.equals(loginViewModel?.username?.get())) {

                        if(!it.uid.isNullOrEmpty()) {

                            FROMUSERID = it.uid
//                            appPreference.FIREBASEUSERID = it.uid

                        }
                    }
                }
            }
        }
        })
    }
}


