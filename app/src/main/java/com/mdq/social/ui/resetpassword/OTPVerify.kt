package com.mdq.social.ui.resetpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mdq.social.databinding.ActivityOtpverifyBinding

class OTPVerify : AppCompatActivity() {
    lateinit var activityOTPVerify:ActivityOtpverifyBinding
    lateinit var storedVerificationId:String
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);
        activityOTPVerify = ActivityOtpverifyBinding.inflate(layoutInflater)
        val view: View = activityOTPVerify!!.getRoot()
        setContentView(view)
        storedVerificationId= intent.getStringExtra("storedVerificationId").toString().trim()
        auth= FirebaseAuth.getInstance()
        activityOTPVerify.DONE.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {

                if(activityOTPVerify.OTP.text.length==6){
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, activityOTPVerify.OTP
                            .text.toString())
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(applicationContext, ResetPasswordActivity::class.java))
                    finish()
                } else {
                        Toast.makeText(this,"Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
