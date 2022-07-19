package com.mdq.social.ui.signupbusiness

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
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
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.signupbusiness.SignupBusinessModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySignupBusinessBinding
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.ui.models.User
import com.mdq.social.ui.signupfreelancer.CategoryNewAdapter
import com.mdq.social.ui.signupfreelancer.FileItem
import com.mdq.social.ui.signupfreelancer.FilesAdapter
import com.mdq.social.utils.FileUtils
import com.mdq.social.utils.UiUtils
import kotlinx.android.synthetic.main.activity_bussiness_update.*
import kotlinx.android.synthetic.main.activity_signup_business.*
import kotlinx.android.synthetic.main.activity_signup_business.cons_category
import kotlinx.android.synthetic.main.activity_signup_business.cons_location
import kotlinx.android.synthetic.main.activity_signup_business.editTextTextPersonName19
import kotlinx.android.synthetic.main.activity_signup_business.img_upload
import kotlinx.android.synthetic.main.activity_signup_business.rdo_Trans
import kotlinx.android.synthetic.main.activity_signup_business.rdo_female
import kotlinx.android.synthetic.main.activity_signup_business.rdo_kids
import kotlinx.android.synthetic.main.activity_signup_business.rdo_male
import kotlinx.android.synthetic.main.activity_signup_business.rv_category
import kotlinx.android.synthetic.main.activity_signup_business.rv_location
import kotlinx.android.synthetic.main.activity_signup_freelancer.*
import kotlinx.android.synthetic.main.dialog_logout.*
import kotlinx.android.synthetic.main.item_for_toast.*
import kotlinx.android.synthetic.main.layout_commnets.view.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class SignUpBusinessActivity :
    BaseActivity<ActivitySignupBusinessBinding, SignupBusinessNavigator>(),
    SignupBusinessNavigator, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener,
    FilesAdapter.ClickManager, CategoryNewAdapter.ClickManager, LocationAdapter.LocationManager,
    LocationNewAdapter.LocationManager, EasyImage.EasyImageStateHandler {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpBusinessActivity::class.java)
        }

        const val IMAGE_PICK_CODE = 999
        const val LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 456
        val LEGACY_WRITE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    private var activitySignUpBusinessBinding: ActivitySignupBusinessBinding? = null
    private var signupBusinessModel: SignupBusinessModel? = null
    private var dialog: Dialog? = null
    private var dialoglocation: Dialog? = null
    private var imageviews: Int? = 0
    private var contentURI: Uri? = null
    private var file: File? = null
    private var categoryList = ArrayList<DataItem>()
    private var locationList = ArrayList<com.mdq.social.app.data.response.location.DataItem>()
    private var fileList = ArrayList<FileItem>()
    private var easyImage: EasyImage? = null
    private val easyImageState = Bundle()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latt: Double = 0.0
    private var lonn: Double = 0.0
    private var monday: Int = 0
    private var tuesday: Int = 0
    private var wednessay: Int = 0
    private var thusday: Int = 0
    private var friday: Int = 0
    private var saturday: Int = 0
    private var sunday: Int = 0
    private var ii: Int = 0
    var signupResponse: SignupResponse? = null
    var category = ""
    var dialoglogout:Dialog?=null
    var USERID:String?=null
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mFireBaseId: String
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var auth: FirebaseAuth
    var number: String=""
    var OTP:Boolean=false

    override fun getLayoutId(): Int {
        return R.layout.activity_signup_business
    }

    override fun getViewBindingVarible(): Int {
        return BR.signupbusinessviewmodel
    }

    override fun getViewModel(): BaseViewModel<SignupBusinessNavigator> {
        signupBusinessModel =
            ViewModelProvider(this, viewModelFactory).get(SignupBusinessModel::class.java)
        return signupBusinessModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBusinessBinding = getViewDataBinding()
        activitySignUpBusinessBinding?.signupbusinessviewmodel = signupBusinessModel
        signupBusinessModel?.navigator = this
        cb_monday.setOnCheckedChangeListener(this)
        cb_tuesday.setOnCheckedChangeListener(this)
        cb_wednesday.setOnCheckedChangeListener(this)
        cb_thurday.setOnCheckedChangeListener(this)
        cb_friday.setOnCheckedChangeListener(this)
        cb_saturday.setOnCheckedChangeListener(this)
        cb_sunday.setOnCheckedChangeListener(this)
        fileList.clear()

        auth= FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
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
                activitySignUpBusinessBinding!!.textViewOTP.visibility = View.VISIBLE
                activitySignUpBusinessBinding!!.CardForEditTextOTP.visibility = View.VISIBLE
                activitySignUpBusinessBinding!!.VerifyOTP.visibility = View.VISIBLE

            }
        }

        activitySignUpBusinessBinding!!.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                activitySignUpBusinessBinding!!.imageView.visibility = View.INVISIBLE
            } else {
                activitySignUpBusinessBinding!!.imageView.visibility = View.VISIBLE
            }
        })

        CardElevation()

        ClickListenerforCheckBox()

        activitySignUpBusinessBinding!!.addCategory.setOnClickListener {

            if (!activitySignUpBusinessBinding!!.editTextTextPersonName8Others.text.isEmpty()) {
                val chip = Chip(this)
                chip.text =
                    activitySignUpBusinessBinding!!.editTextTextPersonName8Others.text.toString()
                        .trim()
                chip.isCloseIconVisible = true
                chip.setTextColor(Color.WHITE)
                chip.chipCornerRadius = 10.0f
                chip.setChipBackgroundColorResource(R.color.txt_pink_chip)
                chip.setOnCloseIconClickListener(this)
                chipGroup2UP.addView(chip)
                chipGroup2UP.visibility = View.VISIBLE
//                category =
//                    category + activitySignUpBusinessBinding!!.editTextTextPersonName8Others.text.toString()
                activitySignUpBusinessBinding!!.editTextTextPersonName8Others.setText("")

            }
        }
    }

    private fun ClickListenerforCheckBox() {
        cb_monday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (monday == 1) {
                            monday = 0
                            cb_monday.isChecked = false
                            tv_monday_start.hint = "Closed"
                            tv_monday_end.hint = "Closed"
                            tv_monday_start.setText("")
                            tv_monday_end.setText("")
                        } else {
                            monday = 1
                            cb_monday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        cb_tuesday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (tuesday == 1) {
                            tuesday = 0
                            cb_tuesday.isChecked = false
                            tv_tuesday_start.hint = "Closed"
                            tv_tuesday_end.hint = "Closed"
                            tv_tuesday_start.setText("")
                            tv_tuesday_end.setText("")
                        } else {
                            tuesday = 1
                            cb_tuesday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        cb_wednesday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (wednessay == 1) {
                            wednessay = 0
                            cb_wednesday.isChecked = false
                            tv_wed_start.hint = "Closed"
                            tv_wed_end.hint = "Closed"
                            tv_wed_start.setText("")
                            tv_wed_end.setText("")
                        } else {
                            wednessay = 1
                            cb_wednesday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        cb_thurday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (thusday == 1) {
                            thusday = 0
                            cb_thurday.isChecked = false
                            tv_thur_start.hint = "Closed"
                            tv_thur_end.hint = "Closed"
                            tv_thur_start.setText("")
                            tv_thur_end.setText("")
                        } else {
                            thusday = 1
                            cb_thurday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        cb_friday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (friday == 1) {
                            friday = 0
                            cb_friday.isChecked = false
                            tv_fri_start.hint = "Closed"
                            tv_fri_end.hint = "Closed"
                            tv_fri_start.setText("")
                            tv_fri_end.setText("")
                        } else {
                            friday = 1
                            cb_friday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        cb_saturday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (saturday == 1) {
                            saturday = 0
                            cb_saturday.isChecked = false
                            tv_sat_start.hint = "Closed"
                            tv_sat_end.hint = "Closed"
                            tv_sat_start.setText("")
                            tv_sat_end.setText("")
                        } else {
                            saturday = 1
                            cb_saturday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })


        cb_sunday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (sunday == 1) {
                            sunday = 0
                            cb_sunday.isChecked = false
                            tv_sun_start.hint = "Closed"
                            tv_sun_end.hint = "Closed"
                            tv_sun_start.setText("")
                            tv_sun_end.setText("")
                        } else {
                            sunday = 1
                            cb_sunday.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun CardElevation() {

        activitySignUpBusinessBinding!!.sentOTP.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                number = activitySignUpBusinessBinding!!.editTextTextPersonName7.text.toString()
                if (number.length == 10) {
                    sendVerificationcode("+91" + number)
                    Toast.makeText(this@SignUpBusinessActivity, "OTP sent to your mobile number.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignUpBusinessActivity, "Enter correct mobile number.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        activitySignUpBusinessBinding!!.VerifyOTP.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (activitySignUpBusinessBinding!!.editTextOTP.text.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, activitySignUpBusinessBinding!!.editTextOTP
                            .text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })

        activitySignUpBusinessBinding!!.editTextTextPersonName.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignUpBusinessBinding!!.editTextTextPersonName2.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignUpBusinessBinding!!.editTextTextPersonName50.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignUpBusinessBinding!!.editTextTextPersonName17.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignUpBusinessBinding!!.editTextTextPersonName18.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignUpBusinessBinding!!.editTextTextPersonName19.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignUpBusinessBinding!!.editTextTextPersonName5.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignUpBusinessBinding!!.editTextTextPersonName6.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignUpBusinessBinding!!.editTextTextPersonName7.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            15F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            0F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
        activitySignUpBusinessBinding!!.editTextTextPersonName8.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.setBackgroundResource(
                            R.drawable.bg_login_back
                        )
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName18.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName6.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.cardElevation =
                            0F
                        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.cardElevation =
                            15F
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    override fun onBackClick() {
        finish()
    }

    override fun onCategoriesClick() {
        if (ii == 0) {
            ii = 1
            if (categoryList.size == 0) {
                signupBusinessModel!!.getCategory().observe(this, { response ->
                    if (response?.data != null) {
                        val categoryResponse = response.data as CategoryResponse
                        if (categoryResponse.message.equals("categories details")) {
                            cons_category.visibility = View.VISIBLE
                            categoryList = categoryResponse.data as ArrayList<DataItem>
                            val categoryAdapter =
                                CategoryNewAdapter(this, categoryList, this)
                            rv_category?.adapter = categoryAdapter

                        } else {
                            showToast(categoryResponse.message!!)
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
        } else if (ii == 1) {
            signupBusinessModel?.onCancelClick()
            ii = 0
        }


    }

    override fun onSignupClick() {
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName17.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.textView25.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.setBackgroundResource(R.drawable.bg_login_back)
        activitySignUpBusinessBinding!!.textView26.setBackgroundResource(R.drawable.bg_login_back)

        if (signupBusinessModel?.name?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName.requestFocus()
            showToast(getString(R.string.please_enter_business_name))
            return
        }

        if (signupBusinessModel?.username?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName50.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName50.requestFocus()
            showToast(getString(R.string.please_enter_username_name))
            return
        }


        if (signupBusinessModel?.useremail?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName2.requestFocus()
            showToast(getString(R.string.please_enter_email))
            return
        }

        if (signupBusinessModel?.password?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName5.requestFocus()
            showToast(getString(R.string.please_enter_password))
            return
        }
        if (signupBusinessModel?.password?.get()?.length!! < 5 || signupBusinessModel?.password?.get()?.length!! > 20) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName5.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName5.requestFocus()
            showToast(getString(R.string.please_enter_characters))
            return
        }

        if (signupBusinessModel?.address?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName19.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName19.requestFocus()
            showToast(getString(R.string.please_enter_address))
            return
        }

        if (signupBusinessModel?.mobilenumber?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName7.requestFocus()
            showToast("please enter valid mobile number")
            return
        }

        if (signupBusinessModel?.mobilenumber?.get()?.length != 10) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName7.requestFocus()
            showToast("please enter valid mobile number")
            return
        }

        val phone=signupBusinessModel?.mobilenumber?.get()

        if (phone!!.startsWith("0") || phone!!.startsWith("1") || phone!!.startsWith("2") ||
            phone!!.startsWith("3") || phone!!.startsWith("4") ||
            phone!!.startsWith("5")) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName7.requestFocus()
            showToast("please enter valid mobile number")
            return
        }
        if (signupBusinessModel?.upload?.get() == null) {
            showToast("Please Choose File")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(signupBusinessModel?.useremail?.get())
                .matches()
        ) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName2.requestFocus()
            showToast(getString(R.string.invalid_email))
            return
        }
        if(!OTP){
            showToast("Please validate your mobile number.")
            return
        }

//        if (signupBusinessModel?.pincode?.get().isNullOrEmpty()) {
//            showToast(getString(R.string.please_enter_pincode))
//            return
//        }

        if (signupBusinessModel?.useremail?.get().isNullOrEmpty()) {
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName2.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName2.requestFocus()
            showToast(getString(R.string.please_enter_email))
            return
        }

//        if (signupBusinessModel?.pincode?.get()!!.length != 6) {
//            activitySignUpBusinessBinding!!.editTextTextPersonName17.setBackgroundResource(R.drawable.bg_delete)
//            activitySignUpBusinessBinding!!.editTextTextPersonName17.requestFocus()
//            showToast(getString(R.string.please_validate_pincode))
//            return
//        }

        var servicesof = ""

        for (i in 0 until chipGroup2UP.childCount) {
            val chip = chipGroup2UP.getChildAt(i) as Chip
            category =
                category + chip.text
                    .toString() + (if (i != chipGroup2UP.childCount - 1) "," else "")
        }

        signupBusinessModel!!.categories.set(category)


        if(signupBusinessModel?.categories?.get().isNullOrEmpty()){
            activitySignUpBusinessBinding!!.CardForEditTextTextPersonName8.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.editTextTextPersonName8.requestFocus()
            showToast("please choose category!")
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
            activitySignUpBusinessBinding!!.textView25.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.textView25.requestFocus()
            showToast(getString(R.string.please_select_services))
            return
        }

        var timingRequest = TimingRequest()

        var timingItemList = ArrayList<TimingItem>()

        if (!cb_monday.isChecked && !cb_tuesday.isChecked
            && !cb_wednesday.isChecked && !cb_thurday.isChecked
            && !cb_friday.isChecked && !cb_saturday.isChecked &&
            !cb_sunday.isChecked
        ) {
            activitySignUpBusinessBinding!!.textView26.setBackgroundResource(R.drawable.bg_delete)
            activitySignUpBusinessBinding!!.textView26.requestFocus()
            showToast(getString(R.string.please_select_timings))
            return
        }

        var ttime: String? = null

        if (cb_monday.isChecked) {

            if (tv_monday_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_monday_time))
                return
            }
            if (tv_monday_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_monday_end_time))
                return
            }

            var timingItem = TimingItem()
            timingItem.day = "monday"
            timingItem.morningTime = tv_monday_start.text.toString()
            timingItem.eveningTime = tv_monday_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            var timingItem = TimingItem()
            timingItem.day = "monday"
            timingItem.morningTime = tv_monday_start.text.toString()
            timingItem.eveningTime = tv_monday_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()

        }


        if (cb_tuesday.isChecked) {

            if (tv_tuesday_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_tuesday_time))
                return
            }
            if (tv_tuesday_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_tuesday_end_time))
                return
            }

            var timingItem = TimingItem()
            timingItem.day = "tuesday"
            timingItem.morningTime = tv_tuesday_start.text.toString()
            timingItem.eveningTime = tv_tuesday_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()

        } else {
            var timingItem = TimingItem()
            timingItem.day = "tuesday"
            timingItem.morningTime = tv_tuesday_start.text.toString()
            timingItem.eveningTime = tv_tuesday_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_wednesday.isChecked) {

            if (tv_wed_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_wednesday_time))
                return
            }
            if (tv_wed_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_wednesday_end_time))
                return
            }

            var timingItem = TimingItem()
            timingItem.day = "wednesday"
            timingItem.morningTime = tv_wed_start.text.toString()
            timingItem.eveningTime = tv_wed_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            var timingItem = TimingItem()
            timingItem.day = "wednesday"
            timingItem.morningTime = tv_wed_start.text.toString()
            timingItem.eveningTime = tv_wed_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }
        if (cb_thurday.isChecked) {

            if (tv_thur_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_thursday_time))
                return
            }
            if (tv_thur_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_thrusday_end_time))
                return
            }

            var timingItem = TimingItem()
            timingItem.day = "thusday"
            timingItem.morningTime = tv_thur_start.text.toString()
            timingItem.eveningTime = tv_thur_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            var timingItem = TimingItem()
            timingItem.day = "thusday"
            timingItem.morningTime = tv_thur_start.text.toString()
            timingItem.eveningTime = tv_thur_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }


        if (cb_friday.isChecked) {

            if (tv_fri_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_friday_time))
                return
            }
            if (tv_fri_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_friday_end_time))
                return
            }

            var timingItem = TimingItem()
            timingItem.day = "friday"
            timingItem.morningTime = tv_fri_start.text.toString()
            timingItem.eveningTime = tv_fri_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {

            var timingItem = TimingItem()
            timingItem.day = "friday"
            timingItem.morningTime = tv_fri_start.text.toString()
            timingItem.eveningTime = tv_fri_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_saturday.isChecked) {

            if (tv_sat_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_saturday_time))
                return
            }
            if (tv_sat_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_saturday_end_time))
                return
            }

            val timingItem = TimingItem()
            timingItem.day = "saturday"
            timingItem.morningTime = tv_sat_start.text.toString()
            timingItem.eveningTime = tv_sat_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            val timingItem = TimingItem()
            timingItem.day = "saturday"
            timingItem.morningTime = tv_sat_start.text.toString()
            timingItem.eveningTime = tv_sat_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_sunday.isChecked) {

            if (tv_sun_start.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_sunday_time))
                return
            }

            if (tv_sun_end.text.toString().isNullOrEmpty()) {
                showToast(getString(R.string.please_select_sunday_end_time))
                return
            }

            val timingItem = TimingItem()
            timingItem.day = "sunday"
            timingItem.morningTime = tv_sun_start.text.toString()
            timingItem.eveningTime = tv_sun_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()

        } else {
            val timingItem = TimingItem()
            timingItem.day = "sunday"
            timingItem.morningTime = tv_sun_start.text.toString()
            timingItem.eveningTime = tv_sun_end.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        val gsonBuilder = GsonBuilder()

        val gson: Gson = gsonBuilder.create()

        timingRequest.shopTime = timingItemList

        val JSONObject: String = gson.toJson(timingItemList)

        signupBusinessModel?.shopTime?.set(JSONObject)
        signupBusinessModel?.services?.set(servicesof)

        activitySignUpBusinessBinding!!.textView2.setFocusable(false)
        signUps()
        toast()

    }

    private fun signUps(){
        signupBusinessModel!!.signUp().observe(this
        ) { response ->

            if (response?.data != null) {
                signupResponse = response.data as SignupResponse

                activitySignUpBusinessBinding!!.textView2.setFocusable(true)

                if (signupResponse!!.message.equals("User added successfully")) {

                    USERID = signupResponse!!.data?.userid

                    performRegitsration(
                        signupBusinessModel?.useremail?.get().toString(),
                        signupBusinessModel?.password?.get().toString()
                    )

                    showToast("User added successfully")

                } else {
                    dialoglogout?.dismiss()

                    showToast(signupResponse!!.message.toString())
                }

            } else {
                dialoglogout?.dismiss()

                showToast(response.throwable?.message!!)
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
                1 -> if (isLegacyExternalStoragePermissionRequired) {
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

    override fun onUploadClick(status: Int) {
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

    override fun onLocationClick() {

        Dexter.withContext(this).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                if (!multiplePermissionsReport.areAllPermissionsGranted()) {
                    denied()
                } else {
                    val manager: LocationManager
                    manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
                                            this@SignUpBusinessActivity,
                                            1001
                                        )
                                    } catch (sendIntentException: SendIntentException) {
                                    }
                                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                    }
                                }
                            }
                        }

                    } else {
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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
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
                        activitySignUpBusinessBinding?.editTextTextPersonName19?.setText(add)
                        signupBusinessModel?.address?.set(add)
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
                            val geocoder =
                                Geocoder(this@SignUpBusinessActivity, Locale.getDefault())
                            var addresses: List<Address>? = null
                            try {
                                addresses = geocoder.getFromLocation(
                                    location1.latitude,
                                    location1.longitude,
                                    1
                                )
                                val add =
                                    (addresses as MutableList<Address>?)?.get(0)?.getAddressLine(0)
                                activitySignUpBusinessBinding?.editTextTextPersonName19?.setText(add)
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

    private fun denied() {

    }

    override fun onDayStartClick(status: Int) {

        when (status) {
            1 -> {

                setTimeDialog(status, true)

            }

            2 -> {

                setTimeDialog(status, true)

            }
            3 -> {

                setTimeDialog(status, true)

            }
            4 -> {

                setTimeDialog(status, true)



            }

            5 -> {

                setTimeDialog(status, true)


            }

            6 -> {

                setTimeDialog(status, true)



            }

            7 -> {

                setTimeDialog(status, true)


            }

        }

    }

    private fun setTimeDialog(status: Int, isFromStart: Boolean) {

        var hours = "0"
        var minute = "0"
        var hoursMinute = "0"
        if (!isFromStart) {
            hoursMinute = UiUtils.getDateReverse(getMinutesandHours(status))
        }

        if (hoursMinute.isNullOrEmpty())
            return

        val timePickerDialog = TimePickerDialog(
            this,
            OnTimeSetListener { view, hourOfDay, minute ->
                setTextField(hourOfDay, minute, status, isFromStart)

            },
            if (isFromStart) 0 else hoursMinute.split(":")[0].toInt(),
            if (isFromStart) 0 else hoursMinute.split(":")[1].toInt(),

            false
        )
        timePickerDialog.show()
    }

    private fun setTextField(hourOfDay: Int, minute: Int, status: Int, isFromStart: Boolean) {
        when (status) {
            1 -> {

                if (isFromStart) {
                    cb_monday.isChecked = true
                    monday = 1
                    tv_monday_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_monday_end.setText("")

                } else {
                    tv_monday_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    if (!tv_monday_start.text.toString().isEmpty() && !tv_monday_end.text.toString()
                            .isEmpty()
                    ) {
                        if (!tv_monday_start.text.toString()
                                .equals(tv_monday_end.text.toString())
                        ) {
                            setAllEdit(
                                tv_monday_start.text.toString(),
                                tv_monday_end.text.toString()
                            )
                        } else {
                            showToast("Select different end time")
                        }
                    }
                }

            }
            2 -> {

                if (isFromStart) {
                    cb_tuesday.isChecked = true
                    tuesday = 1
                    tv_tuesday_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_tuesday_end.setText("")
                } else {
                    tv_tuesday_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }

            }
            3 -> {
                if (isFromStart) {
                    cb_wednesday.isChecked = true
                    wednessay = 1
                    tv_wed_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_wed_end.setText("")
                } else {
                    tv_wed_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }

            }
            4 -> {

                if (isFromStart) {
                    cb_thurday.isChecked = true
                    thusday = 1
                    tv_thur_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_thur_end.setText("")
                } else {
                    tv_thur_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

            5 -> {
                if (isFromStart) {
                    cb_friday.isChecked = true
                    friday = 1
                    tv_fri_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_fri_end.setText("")
                } else {
                    tv_fri_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

            6 -> {
                if (isFromStart) {
                    cb_saturday.isChecked = true
                    saturday = 1
                    tv_sat_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_sat_end.setText("")
                } else {
                    tv_sat_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

            7 -> {
                if (isFromStart) {
                    cb_sunday.isChecked = true
                    sunday = 1
                    tv_sun_start.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_sun_end.setText("")
                } else {
                    tv_sun_end.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

        }

    }

    private fun setAllEdit(date: String, date1: String) {
        activitySignUpBusinessBinding?.SetAllField?.visibility = View.VISIBLE
        activitySignUpBusinessBinding?.SetAllField?.setOnClickListener(object :
            View.OnClickListener {
            override fun onClick(v: View?) {

                tv_tuesday_start.setText(tv_monday_start.text)
                tv_tuesday_end.setText(tv_monday_end.text)
                cb_tuesday.isChecked = true
                tuesday = 1

                tv_wed_start.setText(tv_monday_start.text)
                tv_wed_end.setText(tv_monday_end.text)
                cb_wednesday.isChecked = true
                wednessay = 1

                tv_thur_start.setText(tv_monday_start.text)
                tv_thur_end.setText(tv_monday_end.text)
                cb_thurday.isChecked = true
                thusday = 1

                tv_fri_start.setText(tv_monday_start.text)
                tv_fri_end.setText(tv_monday_end.text)
                cb_friday.isChecked = true
                friday = 1

                tv_sat_start.setText(tv_monday_start.text)
                tv_sat_end.setText(tv_monday_end.text)
                cb_saturday.isChecked = true
                saturday = 1

                tv_sun_start.setText(tv_monday_start.text)
                tv_sun_end.setText(tv_monday_end.text)
                cb_sunday.isChecked = true
                sunday = 1

            }

        })
    }

    private fun getMinutesandHours(status: Int): String {

        when (status) {

            1 -> {
                if (TextUtils.isEmpty(tv_monday_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }

                var hours = tv_monday_start.text.toString()
                return hours
            }
            2 -> {
                if (TextUtils.isEmpty(tv_tuesday_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_tuesday_start.text.toString()
                return hours
            }
            3 -> {
                if (TextUtils.isEmpty(tv_wed_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }

                var hours = tv_wed_start.text.toString()
                return hours
            }
            4 -> {
                if (TextUtils.isEmpty(tv_thur_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_thur_start.text.toString()
                return hours
            }
            5 -> {

                if (TextUtils.isEmpty(tv_fri_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_fri_start.text.toString()
                return hours
            }
            6 -> {
                if (TextUtils.isEmpty(tv_sat_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_sat_start.text.toString()
                return hours
            }
            7 -> {
                if (TextUtils.isEmpty(tv_sun_start.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_sun_start.text.toString()
                return hours
            }

            else -> {
                return ""
            }


        }

    }


    override fun onDayEndClick(status: Int) {
        setTimeDialog(status, false)
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
//        for (i in 0..categoryList.size - 1) {
//            categoryList.get(i).isSelected = false
//        }
        var categoryAdapter = CategoryNewAdapter(this, categoryList, this)
//        rv_category?.adapter = categoryAdapter

        cons_category.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    override fun onConfirmClick() {
        cons_category.visibility = View.GONE

        for (i in 0 until chipGroup2UP.childCount) {
            val chip = chipGroup2UP.getChildAt(i) as Chip
            if (chip.text.equals(categoryList.get(i).name.toString())) {
                showToast("Already added")
                return
            }
        }

        for (i in 0..categoryList.size - 1) {

            if (categoryList.get(i).isSelected!!) {

//                categorySelectedList.add(categoryList.get(i))
                val chip = Chip(this)
                chip.text = categoryList.get(i).name.toString()!!
                chip.setTag(categoryList.get(i).id)
//                val colorcode= ColorStateList.valueOf(Color.parseColor(categoryList.get(i).colorCode))
//                chip.chipBackgroundColor=colorcode
                chip.isCloseIconVisible = true
                chip.setTextColor(Color.WHITE)
                chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if((i % 4) == 0) R.color.mate_red else if((i % 3) == 0) R.color.mate_pink else R.color.txt_pink_chip)
                chip.chipCornerRadius = 10.0f
                chip.setOnCloseIconClickListener(this)
                chipGroup2UP.addView(chip)

            }
        }
    }

    override fun onConfirmLocationClick() {
        cons_location.visibility = View.GONE
        signupBusinessModel?.city?.set(editTextTextPersonName19.text.toString())

        for (i in 0..locationList.size - 1) {
            if (locationList.get(i).isSelected!!) {
                editTextTextPersonName19.setText(locationList.get(i).location)
                break
            }
        }

    }

    override fun onCancelLocationClick() {
        cons_location.visibility = View.GONE
    }


    override fun onItemClick(dataItem: DataItem, position: Int) {
        dialog?.dismiss()

        if (position == 12) {
            activitySignUpBusinessBinding?.consCategory?.visibility = View.GONE
            activitySignUpBusinessBinding?.CardForEditTextTextPersonName8Others?.visibility =
                View.VISIBLE
        } else {
            if (!chipGroup2UP.childCount.equals(null)) {
                for (i in 0 until chipGroup2UP.childCount) {
                    val chip = chipGroup2UP.getChildAt(i) as Chip
                    if (chip.text.equals(dataItem.name)) {
                        showToast("Already added")
                        return
                    }
                }
            }
            val chip = Chip(this)
            chip.text = dataItem.name.toString()
            chip.setTag(dataItem.id)
            chip.setChipBackgroundColorResource(if ((position % 5) == 0) R.color.txt_orange else if((position % 4) == 0) R.color.mate_red else if((position % 3) == 0) R.color.mate_pink else R.color.txt_pink_chip)
            chip.isCloseIconVisible = true
            chip.setTextColor(Color.WHITE)
            chip.chipCornerRadius = 10.0f
            chip.setOnCloseIconClickListener(this)
            chipGroup2UP.addView(chip)
        }

    }

    override fun onItemSelectedClick(dataItemList: DataItem, position: Int, isSelected: Boolean,text:String) {
        dataItemList.isSelected = isSelected
        categoryList.get(position).isSelected = isSelected!!
        if (text.equals("Others")) {
            activitySignUpBusinessBinding?.consCategory?.visibility = View.GONE
            activitySignUpBusinessBinding?.CardForEditTextTextPersonName8Others?.visibility =
                View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        chipGroup2UP.removeView(v)
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
                                val file = File(imageFiles[0].file.absolutePath)
                                getUpload(file)

//                            fileList.clear()
                                fileList.add(FileItem(file))
                                val filess = File(file.absolutePath)
                                if (imageviews == 1) {
                                    signupBusinessModel?.upload?.set(filess)
                                } else {
                                    signupBusinessModel?.profiles?.set(filess)
                                }
                                activitySignUpBusinessBinding?.adapter =
                                    FilesAdapter(
                                        this@SignUpBusinessActivity,
                                        fileList,
                                        this@SignUpBusinessActivity
                                    )
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

                                val file = File(imageFiles[0].file.absolutePath)
                                getUpload(file)
                                img_upload.visibility = View.VISIBLE

//                            fileList.clear()
                                fileList.add(FileItem(file))
                                val filess = File(file.absolutePath)
                                if (imageviews == 1) {
                                    signupBusinessModel?.upload?.set(filess)
                                } else {
                                    signupBusinessModel?.profiles?.set(filess)
                                }
                                activitySignUpBusinessBinding?.adapter =
                                    FilesAdapter(
                                        this@SignUpBusinessActivity,
                                        fileList,
                                        this@SignUpBusinessActivity
                                    )
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

                val path =
                    FileUtils.getPath(this, contentURI)
                img_upload.visibility = View.GONE


                try {
                    file = File(path)
                    getUpload(file!!)

                    img_upload.visibility = View.VISIBLE
                    fileList.add(FileItem(file!!))
                    if (imageviews == 1) {
                        signupBusinessModel?.upload?.set(file)
                    } else {
                        signupBusinessModel?.profiles?.set(file)
                    }
                    activitySignUpBusinessBinding?.adapter =
                        FilesAdapter(this, fileList!!, this)
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this, "" + e, Toast.LENGTH_SHORT)
                }
            }
        }
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

    private fun getUpload(sourceFile: File) {
        if (sourceFile.exists()) {
            val mimeType = if (sourceFile.getAbsolutePath()
                    .contains("jpeg") || sourceFile.getAbsolutePath().contains("jpg")
            ) "image/jpeg" else ""

            if (imageviews == 1) {
                signupBusinessModel?.upload?.set(sourceFile)
                signupBusinessModel?.uploadmimeType?.set(mimeType)
            } else {
                signupBusinessModel?.profiles?.set(sourceFile)
                signupBusinessModel?.profilemimeType?.set(mimeType)
            }

        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.cb_monday -> {
                if (!isChecked) {
                    tv_monday_start.setText("")
                    tv_monday_end.setText("")
                }
            }
            R.id.cb_tuesday -> {
                if (!isChecked) {
                    tv_tuesday_start.setText("")
                    tv_tuesday_end.setText("")
                }
            }
            R.id.cb_wednesday -> {
                if (!isChecked) {
                    tv_wed_start.setText("")
                    tv_wed_end.setText("")
                }
            }

            R.id.cb_thurday -> {
                if (!isChecked) {
                    tv_thur_start.setText("")
                    tv_thur_end.setText("")
                }
            }
            R.id.cb_friday -> {
                if (!isChecked) {
                    tv_fri_start.setText("")
                    tv_fri_end.setText("")
                }
            }
            R.id.cb_saturday -> {
                if (!isChecked) {
                    tv_sat_start.setText("")
                    tv_sat_end.setText("")
                }
            }
            R.id.cb_sunday -> {
                if (!isChecked) {
                    tv_sun_start.setText("")
                    tv_sun_end.setText("")
                }
            }
        }

    }

    override fun onItemClick(
        dataItemList: com.mdq.social.app.data.response.location.DataItem,
        position: Int
    ) {
        dialoglocation?.dismiss()
        editTextTextPersonName19.setText(dataItemList.location)
        signupBusinessModel?.city?.set(dataItemList.location)
        signupBusinessModel?.city?.set(editTextTextPersonName19.text.toString())
    }

    override fun onItemSelectedClick(
        dataItemList: com.mdq.social.app.data.response.location.DataItem,
        position: Int,
        isSelected: Boolean
    ) {

        for (i in 0..locationList.size - 1) {
            locationList.get(i).isSelected = i == position
        }

        Log.v("Location", locationList.size.toString())

        val locationAdapter =
            LocationNewAdapter(this, locationList, this)
        rv_location?.adapter = locationAdapter

    }

    override fun onItemDelterClick(position: Int, fileItem: FileItem) {
        fileList.removeAt(position)
        activitySignUpBusinessBinding?.adapter = FilesAdapter(this, fileList, this)
    }

    override fun restoreEasyImageState(): Bundle {
        return easyImageState
    }

    override fun saveEasyImageState(state: Bundle?) {
    }

    private val isLegacyExternalStoragePermissionRequired: Boolean
        private get() {
            val permissionGranted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            return Build.VERSION.SDK_INT < 29 && !permissionGranted
        }

    private fun requestLegacyWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            LEGACY_WRITE_PERMISSIONS,
            LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
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

        signupBusinessModel?.firebase_UID?.set(uid)
//        signUps()

        firebaseupdateUID(uid)
        val user = if (profileImageUrl == null) {
            User(uid, signupBusinessModel?.username?.get().toString(), null,signupBusinessModel?.mobilenumber?.get().toString(),null)
        } else {
            User(uid, signupBusinessModel?.username?.get().toString(), profileImageUrl,signupBusinessModel?.mobilenumber?.get().toString(),null)
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

        signupBusinessModel!!.UpdateFireBase(USERID!!,uid).observe(this
        ) { response ->
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
