package com.mdq.social.ui.resetpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.databinding.ActivityMoblieNumAdOtpBinding
import java.util.concurrent.TimeUnit

class MoblieNumAdOtp : AppCompatActivity() {
    lateinit var activityMoblieNumAdOtp: ActivityMoblieNumAdOtpBinding
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mFireBaseId: String
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var auth: FirebaseAuth
    var number: String=""
    var preferenceManager1: AppPreference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMoblieNumAdOtp = ActivityMoblieNumAdOtpBinding.inflate(layoutInflater)
        val view: View = activityMoblieNumAdOtp!!.getRoot()
        setContentView(view)
        auth= FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        preferenceManager1= AppPreference(this@MoblieNumAdOtp)

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        if(!preferenceManager1!!.MOBILENU.isNullOrEmpty()){
            activityMoblieNumAdOtp.mobileNum.setText(preferenceManager1!!.MOBILENU)
            activityMoblieNumAdOtp.mobileNum.isEnabled=false
        }

        activityMoblieNumAdOtp.SEND.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                 number = activityMoblieNumAdOtp.mobileNum.text.toString()
                if (number.length == 10) {
                    sendVerificationcode("+91" + number)
                    Toast.makeText(this@MoblieNumAdOtp, "OTP will send to you", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MoblieNumAdOtp, "Enter correct number", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        activityMoblieNumAdOtp?.imageView?.setOnClickListener {
            onBackPressed()
        }

        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.i("gg", "" + e)
                Toast.makeText(applicationContext, "" + e, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                activityMoblieNumAdOtp.Otp.visibility = View.VISIBLE
                activityMoblieNumAdOtp.Verify.visibility = View.VISIBLE
                activityMoblieNumAdOtp.txt1.visibility = View.VISIBLE

            }
        }


        activityMoblieNumAdOtp.Verify.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (activityMoblieNumAdOtp.Otp.text.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, activityMoblieNumAdOtp.Otp
                            .text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })
}

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Verified", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, ResetPasswordActivity::class.java)
                        .putExtra(AppConstants.MOBILENO,number))
                    finish()
                } else {
                    Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun sendVerificationcode(number: String) {

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallBack)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

}
