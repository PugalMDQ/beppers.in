package com.mdq.social.ui.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.signup.SignupViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySignupBinding
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.utils.FileUtils
import com.mdq.social.utils.UiUtils
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import androidx.core.widget.NestedScrollView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.ui.models.User
import com.mdq.social.ui.resetpassword.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_signup.rdo_Trans
import kotlinx.android.synthetic.main.activity_signup.rdo_female
import kotlinx.android.synthetic.main.activity_signup.rdo_male
import kotlinx.android.synthetic.main.item_for_toast.*
import java.util.concurrent.TimeUnit

class  SignUpActivity : BaseActivity<ActivitySignupBinding, SignUpNavigator>(), SignUpNavigator {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    private var activitySignupBinding: ActivitySignupBinding? = null
    private var signupViewModel: SignupViewModel? = null
    private var file: File? = null
    private var contentURI: Uri?=null
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
        return R.layout.activity_signup
    }

    override fun getViewBindingVarible(): Int {
        return BR.signupViewModel
    }

    override fun getViewModel(): BaseViewModel<SignUpNavigator> {
        signupViewModel =
            ViewModelProvider(this, viewModelFactory).get(SignupViewModel::class.java)
        return signupViewModel!!
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignupBinding = getViewDataBinding()
        activitySignupBinding?.signupViewModel = signupViewModel
        signupViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);
        auth= FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        cardElevation()

        activitySignupBinding!!.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                activitySignupBinding!!.imageView.visibility = View.INVISIBLE
            } else {
                activitySignupBinding!!.imageView.visibility = View.VISIBLE
            }
        })

        if (rdo_male.isChecked) {
              rdo_female.isChecked=false
            rdo_Trans.isChecked=false
        }

        if (rdo_female.isChecked) {
            rdo_male.isChecked=false
            rdo_Trans.isChecked=false
        }

        if (rdo_Trans.isChecked) {
            rdo_female.isChecked=false
            rdo_male.isChecked=false
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
                activitySignupBinding!!.textViewOTP.visibility = View.VISIBLE
                activitySignupBinding!!.CardForEditTextOTP.visibility = View.VISIBLE
                activitySignupBinding!!.VerifyOTP.visibility = View.VISIBLE

            }
        }

    }
    @SuppressLint("ClickableViewAccessibility")
    private fun cardElevation() {



        activitySignupBinding!!.sentOTP.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                number = activitySignupBinding!!.editTextTextPersonName7.text.toString()
                if (number.length == 10) {
                    sendVerificationcode("+91" + number)
                    Toast.makeText(this@SignUpActivity, "OTP sent to your mobile number.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignUpActivity, "Enter correct mobile number.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
        activitySignupBinding!!.VerifyOTP.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (activitySignupBinding!!.editTextOTP.text.length == 6) {
                    val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId, activitySignupBinding!!.editTextOTP
                            .text.toString()
                    )
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })
        activitySignupBinding!!.editTextTextPersonName.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignupBinding!!.CardForEditTextTextPersonName.cardElevation=15F
                        activitySignupBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName7.cardElevation=0F}
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignupBinding!!.editTextTextPersonName2.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignupBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName2.cardElevation=15F
                        activitySignupBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName7.cardElevation=0F}

                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignupBinding!!.editTextTextPersonName5.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignupBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName5.cardElevation=15F
                        activitySignupBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName7.cardElevation=0F}

                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignupBinding!!.editTextTextPersonName6.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignupBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName6.cardElevation=15F
                        activitySignupBinding!!.CardForEditTextTextPersonName7.cardElevation=0F}
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        activitySignupBinding!!.editTextTextPersonName7.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        activitySignupBinding!!.CardForEditTextTextPersonName.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName2.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName5.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName6.cardElevation=0F
                        activitySignupBinding!!.CardForEditTextTextPersonName7.cardElevation=15F}

                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun toast() {
        dialoglogout = Dialog(this, R.style.dialog_center)
        dialoglogout?.setCancelable(false)
        dialoglogout?.setContentView(com.mdq.social.R.layout.item_for_toast)
        val textView23 = dialoglogout?.ToastText
        val ok = dialoglogout?.Ok
        textView23?.setText("creating account...")

        dialoglogout?.show()
    }

    override fun backOn() {
        onBackPressed()
    }

    override fun login() {
        startActivity(LoginActivity.getCallingIntent(this))
    }


    override fun profile() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
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

    override fun signUp() {

        activitySignupBinding?.CardForEditTextTextPersonName?.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupBinding?.CardForEditTextTextPersonUserName?.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupBinding?.CardForEditTextTextPersonName2?.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupBinding?.CardForEditTextTextPersonName5?.setBackgroundResource(R.drawable.bg_login_back)
        activitySignupBinding?.CardForEditTextTextPersonName7?.setBackgroundResource(R.drawable.bg_login_back)

        if (signupViewModel?.name?.get().isNullOrEmpty()) {
            activitySignupBinding?.CardForEditTextTextPersonName?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName?.requestFocus()
            showToast("Please enter name")
            return
        }
        if (signupViewModel?.username?.get().isNullOrEmpty()) {
            activitySignupBinding?.CardForEditTextTextPersonUserName?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonUserName?.requestFocus()
            showToast(getString(R.string.please_enter_user))
            return
        }
        if (signupViewModel?.useremail?.get().isNullOrEmpty()) {
            activitySignupBinding?.CardForEditTextTextPersonName2?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName2?.requestFocus()
            showToast(getString(R.string.please_enter_email))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(signupViewModel?.useremail?.get()).matches()) {
            activitySignupBinding?.CardForEditTextTextPersonName2?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName2?.requestFocus()
            showToast(getString(R.string.invalid_email))
            return
        }

        if (signupViewModel?.password?.get().isNullOrEmpty()) {
            activitySignupBinding?.CardForEditTextTextPersonName5?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName5?.requestFocus()
            showToast(getString(R.string.please_enter_password))
            return
        }

        if (signupViewModel?.password?.get()?.length!! < 5 || signupViewModel?.password?.get()?.length!! > 20) {
            activitySignupBinding?.CardForEditTextTextPersonName5?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName5?.requestFocus()
            showToast(getString(R.string.please_enter_characters))
            return
        }

        if (signupViewModel?.mobilenumber?.get().isNullOrEmpty()) {
            activitySignupBinding?.CardForEditTextTextPersonName7?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName7?.requestFocus()
            showToast(getString(R.string.please_enter_mobile_number))
            return
        }
        if(!OTP){
            showToast("Please validate your OTP.")
            return
        }

        val phone=signupViewModel?.mobilenumber?.get()

        if (phone!!.startsWith("0") || phone!!.startsWith("1") || phone!!.startsWith("2") ||
            phone!!.startsWith("3") || phone!!.startsWith("4") ||
            phone!!.startsWith("5")) {
            activitySignupBinding!!.CardForEditTextTextPersonName7.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding!!.editTextTextPersonName7.requestFocus()
            showToast("please enter valid mobile number")
            return
        }

        var servicesof = ""

        if (rdo_male.isChecked) {
            servicesof =  "male"
        }

        if (rdo_female.isChecked) {
            servicesof =  "female"
        }

        if (rdo_Trans.isChecked) {
            servicesof = "transgender"
        }

        signupViewModel?.services?.set(servicesof)

        if (signupViewModel?.mobilenumber?.get()?.length!=10) {
            activitySignupBinding?.editTextTextPersonName7?.setBackgroundResource(R.drawable.bg_delete)
            activitySignupBinding?.editTextTextPersonName7?.requestFocus()
            showToast(getString(R.string.please_enter_phone))
            return
        }
        toast()
        activitySignupBinding!!.textView2.setFocusable(false)

        signUps()
    }

    private fun signUps(){
        signupViewModel!!.signUp().observe(this
        ) { response ->
            if (response?.data != null) {
                val signupResponse = response.data as SignupResponse
                activitySignupBinding!!.textView2.setFocusable(true)

                if (signupResponse.message.equals("User added successfully")) {
                    if (!signupResponse.data?.userid.isNullOrEmpty()) {
                        USERID=signupResponse.data?.userid

                        performRegitsration(
                            signupViewModel?.useremail?.get().toString(),
                            signupViewModel?.password?.get().toString()
                        )
                        showToast(signupResponse.message!!)

                    }
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



    override fun dob() {
        UiUtils.datePickerDialog(
            this,
            System.currentTimeMillis(),
            0,
            object : UiUtils.ClickManager {
                override fun onDatePickDialog(
                    year: Int,
                    monthOfYear: Int,
                    dayofMonth: Int
                ) {

                    val day =
                        if (dayofMonth > 9) dayofMonth.toString() + "" else "0$dayofMonth"

                    val month =
                        if ((monthOfYear + 1) > 9) (monthOfYear + 1).toString() + "" else "0" + (monthOfYear + 1)
                    var dob = "$day-$month-$year"
                    signupViewModel?.dod?.set(dob)

                }

            })
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
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode === 1111) {
                if (data != null) {
                     contentURI= data.data

                    val filePath: String =
                        FileUtils.getPath(this, Uri.parse(contentURI.toString()))
                    val sourceFile = File(filePath)
                    if (sourceFile != null) {
                        getUpload(sourceFile)
                    }

                    file = File(contentURI.toString())
                    val options: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_placeholderimage)
                        .error(R.drawable.ic_placeholderimage)
                        .apply(RequestOptions.signatureOf(ObjectKey(System.currentTimeMillis())))
                        .transform(RoundedCorners(25))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

                }
            } else if (requestCode === 2222) {
                val thumbnail = data!!.extras!!["data"] as Bitmap?

                file = bitmapToFile(this, thumbnail!!, System.currentTimeMillis().toString())
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_placeholderimage)
                    .error(R.drawable.ic_placeholderimage)
                    .apply(RequestOptions.signatureOf(ObjectKey(System.currentTimeMillis())))
                    .transform(RoundedCorners(25))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                Glide.with(this)
                    .load(contentURI)
                    .apply(options)
                    .into(imageView21)
            }
        }
    }

    private fun getUpload(sourceFile: File) {
        if (sourceFile.exists()) {
            val mimeType = if (sourceFile.getAbsolutePath()
                    .contains("jpeg") || sourceFile.getAbsolutePath().contains("jpg")
            ) "image/jpeg" else ""
            signupViewModel?.profileFile?.set(sourceFile)
            signupViewModel?.profilemimeType?.set(mimeType)
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

        signupViewModel?.firebase_UID?.set(uid)
        firebaseupdateUID(uid)

        val user = if (profileImageUrl == null) {
            User(uid, signupViewModel?.username?.get().toString(), null,signupViewModel?.mobilenumber?.get().toString(),null)
        } else {
            User(uid, signupViewModel?.username?.get().toString(), profileImageUrl,signupViewModel?.mobilenumber?.get().toString(),null)
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

        signupViewModel!!.UpdateFireBase(USERID!!,uid).observe(this
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
}