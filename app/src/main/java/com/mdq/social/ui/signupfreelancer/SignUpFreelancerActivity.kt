package com.mdq.social.ui.signupfreelancer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.fasttrack.attachment.helper.upload.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.chip.Chip
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.location.LocationResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.signupfreelancer.SignupFreelancerModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySignupFreelancerBinding
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.ui.models.User
import com.mdq.social.ui.signupbusiness.LocationAdapter
import com.mdq.social.ui.signupbusiness.LocationNewAdapter
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import com.mdq.social.utils.FileUtils
import kotlinx.android.synthetic.main.activity_signup_business.*
import kotlinx.android.synthetic.main.activity_signup_freelancer.*
import kotlinx.android.synthetic.main.activity_signup_freelancer.cons_category
import kotlinx.android.synthetic.main.activity_signup_freelancer.cons_location
import kotlinx.android.synthetic.main.activity_signup_freelancer.editTextTextPersonName19
import kotlinx.android.synthetic.main.activity_signup_freelancer.imageView22
import kotlinx.android.synthetic.main.activity_signup_freelancer.img_upload
import kotlinx.android.synthetic.main.activity_signup_freelancer.rdo_female
import kotlinx.android.synthetic.main.activity_signup_freelancer.rdo_kids
import kotlinx.android.synthetic.main.activity_signup_freelancer.rdo_Trans
import kotlinx.android.synthetic.main.activity_signup_freelancer.rdo_male
import kotlinx.android.synthetic.main.activity_signup_freelancer.rv_category
import kotlinx.android.synthetic.main.activity_signup_freelancer.rv_location
import kotlinx.android.synthetic.main.item_for_toast.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class SignUpFreelancerActivity :
    BaseActivity<ActivitySignupFreelancerBinding, SignupFreelancerNavigator>(),
    SignupFreelancerNavigator, CategoryAdapter.ClickManager, View.OnClickListener,
    LocationAdapter.LocationManager, FilesAdapter.ClickManager, CategoryNewAdapter.ClickManager,
    LocationNewAdapter.LocationManager,EasyImage.EasyImageStateHandler {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpFreelancerActivity::class.java)
        }
    }

    private var activitySignupFreelancerBinding: ActivitySignupFreelancerBinding? = null
    private var signupFreelancerModel: SignupFreelancerModel? = null
    private var dialog: Dialog? = null
    private var file: File? = null
    private var imageviews: Int? = 0
    private var dialoglocation: Dialog? = null
    private var contentURI: Uri? = null
    private var categoryList = java.util.ArrayList<DataItem>()
    private var locationList =
        java.util.ArrayList<com.mdq.social.app.data.response.location.DataItem>()
    private var easyImage: EasyImage? = null
    private val easyImageState = Bundle()
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private var latt:Double = 0.0
    private var lonn:Double = 0.0
    private var ii:Int = 0
    var category = ""
    var dialoglogout:Dialog?=null
    var USERID:String?=null
    var OTP:Boolean=false
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mFireBaseId: String
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var auth: FirebaseAuth
    var number: String=""
    private var fileList = ArrayList<FileItem>()

    override fun getLayoutId(): Int {
        return R.layout.activity_signup_freelancer
    }

    override fun getViewBindingVarible(): Int {
        return BR.loginviewmodel
    }

    override fun getViewModel(): BaseViewModel<SignupFreelancerNavigator> {
        signupFreelancerModel =
            ViewModelProvider(this, viewModelFactory).get(SignupFreelancerModel::class.java)
        return signupFreelancerModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignupFreelancerBinding = getViewDataBinding()
        activitySignupFreelancerBinding?.signupFreelancerviewmodel = signupFreelancerModel
        signupFreelancerModel?.navigator = this

        auth= FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

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
                activitySignupFreelancerBinding!!.textViewOTP.visibility = View.VISIBLE
                activitySignupFreelancerBinding!!.CardForEditTextOTP.visibility = View.VISIBLE
                activitySignupFreelancerBinding!!.VerifyOTP.visibility = View.VISIBLE

            }
        }

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        easyImage = EasyImage.Builder(this)
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(true)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("Makeovers")
            .allowMultiple(true)
            .setStateHandler(this)
            .build()

        activitySignupFreelancerBinding?.rdoTrevelYes?.setOnClickListener {
            signupFreelancerModel?.travel?.set("Travel other cities")
        }
        activitySignupFreelancerBinding?.rdoTrevelNo?.setOnClickListener {
            signupFreelancerModel!!.travel.set("Can't travel other cities")
        }

        activitySignupFreelancerBinding!!.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                activitySignupFreelancerBinding!!.imageView.visibility = View.INVISIBLE
            } else {
                activitySignupFreelancerBinding!!.imageView.visibility = View.VISIBLE
            }
        })

         activitySignupFreelancerBinding!!.addCategory.setOnClickListener {

             if (!activitySignupFreelancerBinding!!.editTextTextPersonName8Others.text.isEmpty()) {
                 val chip = Chip(this)
                 chip.text =
                     activitySignupFreelancerBinding!!.editTextTextPersonName8Others.text.toString()
                         .trim()
                 chip.isCloseIconVisible = true
                 chip.setTextColor(Color.WHITE)
                 chip.chipCornerRadius = 10.0f
                 chip.setChipBackgroundColorResource(R.color.txt_orange )
                 chip.setOnCloseIconClickListener(this)
                 chipGroup.addView(chip)
                 chipGroup.visibility = View.VISIBLE
                 activitySignupFreelancerBinding!!.editTextTextPersonName8Others.setText("")
             }
         }

        CardElevation()

    }
    @SuppressLint("ClickableViewAccessibility")
    private fun CardElevation() {
        activitySignupFreelancerBinding!!.sentOTP.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                number = activitySignupFreelancerBinding!!.editTextTextPersonName7.text.toString()
                if (number.length == 10) {
                    sendVerificationcode("+91" + number)
                    Toast.makeText(this@SignUpFreelancerActivity, "OTP sent to your mobile number.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignUpFreelancerActivity, "Enter correct mobile number.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        activitySignupFreelancerBinding!!.VerifyOTP.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (activitySignupFreelancerBinding!!.editTextOTP.text.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, activitySignupFreelancerBinding!!.editTextOTP
                            .text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })

        activitySignupFreelancerBinding!!.editTextTextPersonName.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName17.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName18.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName19.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName2.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName5.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName6.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName7.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName50.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=15F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=0F

                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignupFreelancerBinding!!.editTextTextPersonName8.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN->{
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName18.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.cardElevation=0F
                       activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.cardElevation=15F
                        }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    override fun onBackClick() {
        finish()
    }

    override fun onSignupClick() {

        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName17.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.setBackgroundResource(R.drawable.bg_login_back)

        if (signupFreelancerModel?.name?.get().isNullOrEmpty()) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName.requestFocus()
            showToast(getString(R.string.please_enter_name))
            return
        }

        if (signupFreelancerModel?.username?.get().isNullOrEmpty()) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName50.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName50.requestFocus()
            showToast(getString(R.string.please_enter_username_name))
            return
        }

        if (signupFreelancerModel?.password?.get().isNullOrEmpty()) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName5.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName5.requestFocus()
            showToast(getString(R.string.please_enter_password))
            return
        }

        if (signupFreelancerModel?.password?.get()?.length!! < 5 || signupFreelancerModel?.password?.get()?.length!! > 10) {
            showToast(getString(R.string.please_enter_characters))
            return
        }

        if (signupFreelancerModel?.address?.get().isNullOrEmpty()) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName19.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName19.requestFocus()
            showToast(getString(R.string.please_enter_address))
            return
        }

        if (signupFreelancerModel?.mobilenumber?.get().isNullOrEmpty()) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName7.requestFocus()
            showToast(getString(R.string.please_enter_mobile_number))
            return
        }

        val phone=signupFreelancerModel?.mobilenumber?.get()

        if (phone!!.startsWith("0") || phone!!.startsWith("1") || phone!!.startsWith("2") ||
            phone!!.startsWith("3") || phone!!.startsWith("4") ||
            phone!!.startsWith("5")) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName7.requestFocus()
            showToast("please enter valid mobile number")
            return
        }

        if (signupFreelancerModel?.mobilenumber?.get()?.length!! != 10) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName7.requestFocus()
            showToast(getString(R.string.please_enter_phone))
            return
        }

        if(!OTP){
            showToast("Please validate your mobile number.")
            return
        }


//        if (signupFreelancerModel?.pincode?.get().isNullOrEmpty()) {
//            showToast(getString(R.string.please_enter_pincode))
//            return
//        }

        if (signupFreelancerModel?.useremail?.get().isNullOrEmpty()) {
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName2.requestFocus()
            showToast(getString(R.string.please_enter_email))
            return
        }

        if (! android.util.Patterns.EMAIL_ADDRESS.matcher(signupFreelancerModel?.useremail?.get()).matches()) {
             activitySignupFreelancerBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName2.requestFocus()
            showToast(getString(R.string.invalid_email))
            return
        }

//        if (signupFreelancerModel?.pincode?.get()!!.length != 6) {
//            showToast(getString(R.string.please_validate_pincode))
//            return
//        }

        if(signupFreelancerModel?.upload?.get()==null){
            showToast("Please Choose File")
            return
        }

        var servicesof = ""
        if(chipGroup.childCount!=null) {
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                category =
                    category + chip.text
                        .toString() + (if (i != chipGroup.childCount - 1) "," else "")
            }
        }
        if (category.isNullOrEmpty()) {
            showToast(getString(R.string.please_select_categories))
            return
        }

        if (rdo_male.isChecked) {
            servicesof = servicesof + "Male"
        }
        if (rdo_female.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "female"
        }
        if (rdo_Trans.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "trans"
        }
        if (rdo_kids.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "kids"
        }
        if (servicesof.isNullOrEmpty()) {
            showToast(getString(R.string.please_select_services))
            return
        }
        signupFreelancerModel?.categories?.set(category)

        if(signupFreelancerModel?.categories?.get().isNullOrEmpty()){
            activitySignupFreelancerBinding!!.CardForEditTextTextPersonName8.setBackgroundResource(R.drawable.bg_delete)
            activitySignupFreelancerBinding!!.editTextTextPersonName8.requestFocus()
            showToast("please choose category!")
            return
        }

        signupFreelancerModel?.services?.set(servicesof)
        activitySignupFreelancerBinding!!.textView2.setFocusable(false)

        toast()
        signUps()

    }
    private fun signUps(){
        signupFreelancerModel!!.signUp().observe(this) { response ->
            if (response?.data != null) {
                val signupResponse = response.data as SignupResponse
                activitySignupFreelancerBinding!!.textView2.setFocusable(true)

                if (signupResponse.message.equals("User added successfully")) {
                    USERID = signupResponse!!.data?.userid

                    performRegitsration(
                        signupFreelancerModel?.useremail?.get().toString(),
                        signupFreelancerModel?.password?.get().toString()
                    )
                } else {

                    showToast(signupResponse!!.message.toString())
                    dialoglogout?.dismiss()

                }

            } else {
                showToast(response.throwable?.message!!)
                dialoglogout?.dismiss()

            }
        }
    }

    private fun toast() {
        dialoglogout = Dialog(this, R.style.dialog_center)
        dialoglogout?.setCancelable(false)
        dialoglogout?.setContentView(com.mdq.social.R.layout.item_for_toast)
        val textView23 = dialoglogout?.ToastText
        val ok = dialoglogout?.Ok
        textView23?.setText("Creating a account")

        dialoglogout?.show()
    }

    private fun setDialogLocation(locationResponse: LocationResponse) {
        dialoglocation = Dialog(this, R.style.dialog_center)
        dialoglocation?.setContentView(R.layout.dialog_category)

        val rvCategory = dialoglocation?.findViewById<RecyclerView>(R.id.rv_category)
        val textView71 = dialoglocation?.findViewById<TextView>(R.id.textView71)
        rvCategory?.layoutManager = LinearLayoutManager(this)

        val locationAdapter = LocationAdapter(
            this,
            locationResponse.data as List<com.mdq.social.app.data.response.location.DataItem>,
            this
        )
        rvCategory?.adapter = locationAdapter
        dialoglocation?.show()
    }

    override fun onCategoryClick() {
        if(ii==0) {
            ii=1
            if (categoryList.size == 0) {
                signupFreelancerModel!!.getCategory().observe(this, { response ->
                    if (response?.data != null) {
                        val signupResponse = response.data as CategoryResponse
                        if (signupResponse.message.equals("categories details")) {
                            cons_category.visibility = View.VISIBLE
                            categoryList = signupResponse.data as java.util.ArrayList<DataItem>
                            val categoryAdapter =
                                CategoryNewAdapter(this, categoryList, this)
                            rv_category?.adapter = categoryAdapter
                        } else {
                            showToast(signupResponse.message!!)
                        }
                    } else {
                        showToast(response.throwable?.message!!)
                    }
                })

            } else {
                cons_category.visibility = View.VISIBLE

                val categoryAdapter =
                    CategoryNewAdapter(this, categoryList, this)
                rv_category?.adapter = categoryAdapter
            }
        }

        else{
            signupFreelancerModel?.onCancelClick()
            ii=0
        }

    }

    private fun getUpload(sourceFile: File) {
        if (sourceFile.exists()) {
            val mimeType = if (sourceFile.getAbsolutePath()
                    .contains("jpeg") || sourceFile.getAbsolutePath().contains("jpg")
            ) "image/jpeg" else ""
            signupFreelancerModel?.upload?.set(sourceFile)
            signupFreelancerModel?.uploadmimeType?.set(mimeType)
        }
    }

    override fun onUploadClick(status: Int) {
        imageviews = status
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        if (fileList.size > 1) {
                            showToast("Please remove some items")
                            return
                        }
                        showPictureDialog()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    override fun onLocationClick() {

        Dexter.withContext(this).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                } else {
                    val manager: LocationManager
                    manager = getSystemService( Context.LOCATION_SERVICE ) as LocationManager

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        val locationRequest = LocationRequest.create()
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationRequest.interval = 2000
                        locationRequest.fastestInterval = 1500

                        val builder = LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest)
                        builder.setAlwaysShow(true)
                        val requestTask = LocationServices.getSettingsClient(
                            applicationContext
                        )
                            .checkLocationSettings(builder.build())

                        requestTask.addOnCompleteListener { task ->
                            try {
                                val response = task.getResult(ApiException::class.java)
                                getsome()
                            } catch (e: ApiException) {
                                when (e.statusCode) {
                                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                        val resolvableApiException = e as ResolvableApiException
                                        resolvableApiException.startResolutionForResult(
                                            this@SignUpFreelancerActivity,
                                            1001
                                        )
                                    } catch (sendIntentException: IntentSender.SendIntentException) {
                                    }
                                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                    }
                                }
                            }
                        }

                    }else {
                        getsome()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()

    }

    private fun getsome() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.getLastLocation()
            .addOnCompleteListener(OnCompleteListener<Location?> { task ->
                val location = task.result
                if (location != null) {
                    latt = location.latitude
                    lonn = location.longitude
                    val geocoder = Geocoder(this, Locale.getDefault())
                    var addresses: List<Address>? = null
                    try {
                        addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val add = addresses[0].getAddressLine(0)
                        activitySignupFreelancerBinding?.editTextTextPersonName19?.setText(add)
                    } catch (e: IOException) {
                    }
                } else {
                    val locationRequest: LocationRequest = LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10000)
                        .setFastestInterval(1000)
                        .setNumUpdates(1)
                    val locationCallbac: LocationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            val location1: Location = locationResult.getLastLocation()
                            latt = location1.latitude
                            lonn = location1.longitude
                            val geocoder = Geocoder(this@SignUpFreelancerActivity, Locale.getDefault())
                            var addresses: List<Address>? = null
                            try {
                                addresses = geocoder.getFromLocation(
                                    location1.latitude,
                                    location1.longitude,
                                    1
                                )
                                val add = (addresses as MutableList<Address>?)?.get(0)?.getAddressLine(0)
                                activitySignupFreelancerBinding?.editTextTextPersonName19?.setText(add)
                                signupFreelancerModel?.address?.set(editTextTextPersonName19?.text.toString())
                                signupFreelancerModel?.city?.set(activitySignupFreelancerBinding?.editTextTextPersonName6?.text.toString())

                            } catch (e: IOException) {
                            }
                        }
                    }
                    Looper.myLooper()?.let {
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallbac,
                            it
                        )
                    }
                }
            })
    }


    override fun profileClick(status: Int) {
        imageviews = status
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showToast(getString(R.string.all_permissions_granted))
                        showPictureDialog()

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()

                }
            }).check()
    }

    override fun onCancelClick() {
        for (i in 0..categoryList.size - 1) {
            categoryList.get(i).isSelected = false
        }
        var categoryAdapter = CategoryNewAdapter(this, categoryList, this)
        rv_category?.adapter = categoryAdapter

        cons_category.visibility = View.GONE
    }

    override fun onConfirmClick() {
        cons_category.visibility = View.GONE

        val dataItem:DataItem= DataItem()
        for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                if (chip.text.equals(categoryList.get(i).name.toString())) {
                    showToast("Already added")
                    return
                }
            }
        for (i in 0..categoryList.size - 1) {
            if (categoryList.get(i).isSelected!!) {

                val chip = Chip(this)
                chip.text = categoryList.get(i).name.toString()
                chip.setTag(categoryList.get(i).id)

                chip.isCloseIconVisible = true
                chip.setTextColor(Color.WHITE)
                chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if((i % 4) == 0) R.color.mate_red else if((i % 3) == 0) R.color.mate_pink else R.color.txt_pink_chip)
                chip.chipCornerRadius = 10.0f
                chip.setOnCloseIconClickListener(this)
                chipGroup.addView(chip)

                }

        }


    }

    override fun onConfirmLocationClick() {
        cons_location.visibility = View.GONE

        for (i in 0..locationList.size - 1) {
            if (locationList.get(i).isSelected!!) {
                editTextTextPersonName19.setText(locationList.get(i).location)
                signupFreelancerModel?.city?.set(locationList.get(i).location)
                break
            }
        }

    }

    override fun onCancelLocationClick() {
        cons_location.visibility = View.GONE

    }


    private fun setDialogCategory(categoryResponse: CategoryResponse) {
        dialog = Dialog(this, R.style.dialog_center)
        dialog?.setContentView(R.layout.dialog_category)

        val rvCategory = dialog?.findViewById<RecyclerView>(R.id.rv_category)
        val textView71 = dialoglocation?.findViewById<TextView>(R.id.textView71)
        textView71?.text = getString(R.string.choose_location)
        rvCategory?.layoutManager = LinearLayoutManager(this)

        val categoryAdapter = CategoryAdapter(this, categoryResponse.data as List<DataItem>, this)
        rvCategory?.adapter = categoryAdapter
        dialog?.show()
    }

    override fun onItemClick(dataItem: DataItem, position: Int) {
        dialog?.dismiss()
        if(position==12){
            activitySignupFreelancerBinding?.consCategory?.visibility=View.GONE
            activitySignupFreelancerBinding?.CardForEditTextTextPersonName8Others?.visibility=View.VISIBLE
        }else {
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                if (chip.text.equals(dataItem.name)) {
                    showToast("Already added")
                    return
                }
            }

            val chip = Chip(this)
            chip.text = dataItem.name.toString()
            chip.setTag(dataItem.id)
            chip.setTag(dataItem.id)
            chip.setChipBackgroundColorResource(if ((position % 5) == 0) R.color.txt_orange else if((position % 4) == 0) R.color.mate_red else if((position % 3) == 0) R.color.mate_pink else R.color.txt_pink_chip)
            chip.isCloseIconVisible = true
            chip.setTextColor(Color.WHITE)
            chip.chipCornerRadius = 10.0f
            chip.setOnCloseIconClickListener(this)
            chipGroup.addView(chip)
        }

    }

    override fun onItemSelectedClick(dataItemList: DataItem, position: Int, isSelected: Boolean,text:String) {
        dataItemList.isSelected = isSelected
        categoryList.get(position).isSelected=isSelected!!
        if (text.equals("Others")) {
            activitySignupFreelancerBinding?.consCategory?.visibility=View.GONE
            activitySignupFreelancerBinding?.CardForEditTextTextPersonName8Others?.visibility=View.VISIBLE
        }
    }


    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems =
            arrayOf("Select from storage", "Capture from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> if (isLegacyExternalStoragePermissionRequired) {
                    requestLegacyWriteExternalStoragePermission()
                } else {
                    easyImage!!.openGallery(this)
                }
                1 ->  if (isLegacyExternalStoragePermissionRequired) {
                    requestLegacyWriteExternalStoragePermission()
                } else {
                    easyImage!!.openCameraForImage(this)
                }
            }
        }
        pictureDialog.show()
    }

    private fun getFiles() {
        val intent = Intent()
        intent.type = "*/*"

        val ACCEPT_MIME_TYPES = arrayOf(
            "application/pdf"
        )
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(intent, 3333)

    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 1111)

    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 2222)
    }

    override fun onClick(v: View?) {
        chipGroup.removeView(v)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode === RequestCodes.PICK_PICTURE_FROM_GALLERY) {

                easyImage!!.handleActivityResult(
                    requestCode,
                    resultCode,
                    data,
                    this,
                    object : DefaultCallback() {
                        override fun onMediaFilesPicked(
                            imageFiles: Array<MediaFile>,
                            source: MediaSource
                        ) {
                            lifecycleScope.launch {

                                file = File(imageFiles[0].file.absolutePath)
                                getUpload(file!!)
                                if (imageviews == 1) {
                                    img_upload.visibility = View.VISIBLE
                                    fileList.add(FileItem(file!!))
                                    signupFreelancerModel?.upload?.set(file!!)
                                    activitySignupFreelancerBinding?.adapter =
                                        FilesAdapter(
                                            this@SignUpFreelancerActivity,
                                            fileList!!,
                                            this@SignUpFreelancerActivity
                                        )

                                } else {
                                    val options: RequestOptions = RequestOptions()
                                        .placeholder(R.drawable.ic_placeholderimage)
                                        .error(R.drawable.ic_placeholderimage)
                                        .apply(RequestOptions.signatureOf(ObjectKey(System.currentTimeMillis())))
                                        .transform(RoundedCorners(25))
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    Glide.with(this@SignUpFreelancerActivity)
                                        .load(file?.absolutePath)
                                        .apply(options)
                                        .into(imageView22)

                                }
                            }
                        }

                        override fun onImagePickerError(error: Throwable, source: MediaSource) {
                            error.printStackTrace()
                        }

                        override fun onCanceled(source: MediaSource) {
                        }
                    })

            } else if (requestCode === RequestCodes.TAKE_PICTURE) {

                easyImage!!.handleActivityResult(
                    requestCode,
                    resultCode,
                    data,
                    this,
                    object : DefaultCallback() {
                        override fun onMediaFilesPicked(
                            imageFiles: Array<MediaFile>,
                            source: MediaSource
                        ) {
                            lifecycleScope.launch {
                                val files = File(imageFiles[0].file.absolutePath)
                                getUpload(files)

                                if (imageviews == 1) {
                                    img_upload.visibility = View.VISIBLE
                                    fileList.clear()
                                    fileList.add(FileItem(files))
                                    signupFreelancerModel?.upload?.set(files!!)
                                    activitySignupFreelancerBinding?.adapter = FilesAdapter(
                                        this@SignUpFreelancerActivity,
                                        fileList!!,
                                        this@SignUpFreelancerActivity
                                    )

                                } else {
                                    val options: RequestOptions = RequestOptions()
                                        .placeholder(R.drawable.ic_placeholderimage)
                                        .error(R.drawable.ic_placeholderimage)
                                        .apply(RequestOptions.signatureOf(ObjectKey(System.currentTimeMillis())))
                                        .transform(RoundedCorners(25))
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    Glide.with(this@SignUpFreelancerActivity)
                                        .load(file?.absolutePath)
                                        .apply(options)
                                        .into(imageView22)
                                }

                                val file = File(imageFiles[0].file.absolutePath)
                                getUpload(file)
                                img_upload.visibility = View.VISIBLE

                            }
                        }

                        override fun onImagePickerError(error: Throwable, source: MediaSource) {
                            error.printStackTrace()
                        }

                        override fun onCanceled(source: MediaSource) {
                        }
                    })

            } else if (requestCode == 3333) {
                contentURI = data?.data
                img_upload.visibility = View.GONE

                val path =
                    FileUtils.getPath(this, contentURI)

                file = File(path)
                fileList.clear()
                fileList.add(FileItem(file!!))
                signupFreelancerModel?.upload?.set(file!!)

                activitySignupFreelancerBinding?.adapter = FilesAdapter(this, fileList!!, this)

            }

        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Verified", Toast.LENGTH_SHORT).show()

                    OTP=true
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


    fun bitmapToFile(
        context: Context?,
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata: ByteArray = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    override fun onItemClick(
        dataItemList: com.mdq.social.app.data.response.location.DataItem,
        position: Int
    ) {
        dialoglocation?.dismiss()
        signupFreelancerModel?.city?.set(dataItemList.location)
    }

    override fun onItemSelectedClick(
        dataItemList: com.mdq.social.app.data.response.location.DataItem,
        position: Int,
        isSelected: Boolean
    ) {
        for (i in 0..locationList.size - 1) {
            locationList.get(i).isSelected = i == position
        }

        Log.v("Location",locationList.size.toString())

        val locationAdapter =
            LocationNewAdapter(this, locationList, this)
        rv_location?.adapter = locationAdapter
    }

    override fun onItemDelterClick(position: Int, fileItem: FileItem) {
        fileList.removeAt(position);
        activitySignupFreelancerBinding?.adapter = FilesAdapter(this, fileList, this)

    }

    override fun restoreEasyImageState(): Bundle {
        return easyImageState
    }

    override fun saveEasyImageState(state: Bundle?) {

    }
    private val isLegacyExternalStoragePermissionRequired: Boolean
        private get() {
            val permissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            return Build.VERSION.SDK_INT < 29 && !permissionGranted
        }

    private fun requestLegacyWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(this,
            SignUpBusinessActivity.LEGACY_WRITE_PERMISSIONS,
            SignUpBusinessActivity.LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
        )
    }


    private fun performRegitsration(email:String, password:String) {

        if(email!=null && password!=null) {
            // Firebase Authentication to create a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    saveUserToFirebaseDatabase(null)
                }
                .addOnFailureListener {
                    Log.d("TAG", "Failed to create user: ${it.message}")
                    showToast("${it.message}")
                }
        }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String?) {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        signupFreelancerModel?.firebase_UID?.set(uid)
//        signUps()
        firebaseupdateUID(uid)

        val user = if (profileImageUrl == null) {
            User(uid, signupFreelancerModel?.username?.get().toString(), null,signupFreelancerModel?.mobilenumber?.get().toString(),null)
        } else {
            User(uid, signupFreelancerModel?.username?.get().toString(), profileImageUrl,signupFreelancerModel?.mobilenumber?.get().toString(),null)
        }

        ref.setValue(user)
            .addOnSuccessListener {

                Log.d(AppSignatureHelper.TAG, "Finally we saved the user to Firebase Database")
            }
            .addOnFailureListener {
                dialoglogout?.dismiss()
                Log.d(AppSignatureHelper.TAG, "Failed to set value to database: ${it.message}")
            }

    }

    private fun firebaseupdateUID(uid: String) {

        signupFreelancerModel!!.UpdateFireBase(USERID!!,uid).observe(this)
        { response ->
            if (response?.data != null) {
                val signupResponse = response.data as SignupResponse
                dialoglogout?.dismiss()

                if (signupResponse.message.equals("User updated successfully!")) {
                    startActivity(LoginActivity.getCallingIntent(this))
                    finish()
                } else {
                    showToast(signupResponse!!.message.toString())
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        }

    }

}