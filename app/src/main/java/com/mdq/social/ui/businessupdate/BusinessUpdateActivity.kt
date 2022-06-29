package com.mdq.social.ui.businessupdate

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.fasttrack.attachment.helper.upload.*
import com.fasttrack.attachment.helper.upload.RequestCodes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.ShopItem
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.businessupdate.BusinessUpdateViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityBussinessUpdateBinding
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import com.mdq.social.ui.signupbusiness.TimingItem
import com.mdq.social.ui.signupbusiness.TimingRequest
import com.mdq.social.ui.signupfreelancer.CategoryNewAdapter
import com.mdq.social.ui.signupfreelancer.FileItem
import com.mdq.social.ui.signupfreelancer.FilesAdapter
import com.mdq.social.utils.FileUtils
import com.mdq.social.utils.UiUtils
import kotlinx.android.synthetic.main.activity_bussiness_update.*
import kotlinx.android.synthetic.main.activity_freelance_update.*
import kotlinx.android.synthetic.main.activity_signup_business.*
import kotlinx.android.synthetic.main.activity_signup_business.img_upload
import kotlinx.android.synthetic.main.activity_signup_business.rv_category
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class BusinessUpdateActivity:BaseActivity<ActivityBussinessUpdateBinding, BusinessUpdateNavigator>(),EasyImage.EasyImageStateHandler,
    FilesAdapter.ClickManager,CategoryNewAdapter.ClickManager,
    CompoundButton.OnCheckedChangeListener,
    BusinessUpdateNavigator, View.OnClickListener {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, BusinessUpdateActivity::class.java)
        }
    }
    private var atcivityBussinessUpdateBinding: ActivityBussinessUpdateBinding? = null
    private var businessUpdateViewModel: BusinessUpdateViewModel? = null
    private var easyImage: EasyImage? = null
    private var fileList = ArrayList<FileItem>()
    private var imageviews: Int? = 1
    private var contentURI: Uri? = null
    private var file: File? = null
    private val easyImageState = Bundle()
    private var monday:Int=0
    private var tuesday:Int=0
    private var wednessay:Int=0
    private var thusday:Int=0
    private var friday:Int=0
    private var saturday:Int=0
    private var sunday:Int=0
    private var categoryList = ArrayList<DataItem>()
    private var ii:Int=0
    var category = ""
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private var latt:Double = 0.0
    private var lonn:Double = 0.0
    override fun getLayoutId(): Int {
        return R.layout.activity_bussiness_update
    }

    override fun getViewBindingVarible(): Int {
        return BR.businessUpdateViewModel
    }

    override fun getViewModel(): BaseViewModel<BusinessUpdateNavigator> {
        businessUpdateViewModel =
            ViewModelProvider(this, viewModelFactory).get(BusinessUpdateViewModel::class.java)
        return businessUpdateViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        atcivityBussinessUpdateBinding = getViewDataBinding()
        atcivityBussinessUpdateBinding?.businessUpdateViewModel = businessUpdateViewModel
        businessUpdateViewModel?.navigator = this

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

        getProfileDetail()
        ClickListenerforCheckBox()


        atcivityBussinessUpdateBinding!!.cbMondayUP.setPadding(40,20,20,20);


        atcivityBussinessUpdateBinding?.address?.setOnClickListener {
            onLocationClicks()
        }

        atcivityBussinessUpdateBinding?.imageView?.setOnClickListener {

            onBackPressed()
        }
        atcivityBussinessUpdateBinding?.textView2?.setOnClickListener {
            updateClick()
        }

        atcivityBussinessUpdateBinding?.textView51?.setOnClickListener {
            uploadClick()
        }

        atcivityBussinessUpdateBinding!!.addCategory.setOnClickListener {

            if(!atcivityBussinessUpdateBinding!!.editTextTextPersonName8Others.text.isEmpty()) {
                val chip = Chip(this)
                chip.text =
                    atcivityBussinessUpdateBinding!!.editTextTextPersonName8Others.text.toString()
                        .trim()+","
                chip.isCloseIconVisible = true
                chip.setTextColor(Color.WHITE)
                chip.chipCornerRadius = 10.0f
                chip.setChipBackgroundColorResource(R.color.txt_pink)
                chip.setOnCloseIconClickListener(this)
                chipGroup2.addView(chip)
                chipGroup2.visibility = View.VISIBLE
                atcivityBussinessUpdateBinding!!.editTextTextPersonName8Others.setText("")

            }
        }

    }
    private fun onLocationClicks() {

        Dexter.withContext(this).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                if (!multiplePermissionsReport.areAllPermissionsGranted()) {

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
                                            this@BusinessUpdateActivity,
                                            1001
                                        )
                                    } catch (sendIntentException: IntentSender.SendIntentException) {
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
                        atcivityBussinessUpdateBinding?.editTextTextPersonName19?.setText(add)
                        businessUpdateViewModel?.address?.set(add)
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
                            lonn = location1.longitude
                            val geocoder =
                                Geocoder(this@BusinessUpdateActivity, Locale.getDefault())
                            var addresses: List<Address>? = null
                            try {
                                addresses = geocoder.getFromLocation(
                                    location1.latitude,
                                    location1.longitude,
                                    1
                                )
                                val add =
                                    (addresses as MutableList<Address>?)?.get(0)?.getAddressLine(0)
                                atcivityBussinessUpdateBinding?.editTextTextPersonName19?.setText(add)
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

    private fun getProfileDetail(){
        businessUpdateViewModel!!.getUserProfileDetails(appPreference.USERID.toString()).observe(this, Observer { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileDetailResponse
        atcivityBussinessUpdateBinding?.editTextTextPersonName50?.setText(userProfileResponse?.data?.get(0)?.user_name)
        atcivityBussinessUpdateBinding?.editTextTextPersonName2?.setText(userProfileResponse?.data?.get(0)?.email)
        atcivityBussinessUpdateBinding?.editTextTextPersonName5?.setText(userProfileResponse?.data?.get(0)?.password)
        atcivityBussinessUpdateBinding?.editTextTextPersonName19?.setText(userProfileResponse?.data?.get(0)?.address)
        atcivityBussinessUpdateBinding?.editTextTextPersonName7?.setText(userProfileResponse?.data?.get(0)?.mobile)
        atcivityBussinessUpdateBinding?.editTextTextPersonName?.setText(userProfileResponse?.data?.get(0)?.name)
        atcivityBussinessUpdateBinding?.editTextTextPersonName18?.setText(userProfileResponse?.data?.get(0)?.description)

                if(userProfileResponse.data?.get(0)?.service?.contains("Male")!!){
                    atcivityBussinessUpdateBinding?.rdoMale?.isChecked=true
                }
                if(userProfileResponse.data?.get(0)?.service?.contains("female")!!){
                    atcivityBussinessUpdateBinding?.rdoFemale?.isChecked=true
                }
                if(userProfileResponse.data?.get(0)?.service?.contains("kids")!!){
                    atcivityBussinessUpdateBinding?.rdoKids?.isChecked=true
                }
                if(userProfileResponse.data?.get(0)?.service?.contains("trans")!!){
                    atcivityBussinessUpdateBinding?.rdoTrans?.isChecked=true
                }
                var uri: ArrayList<String>?= ArrayList()
                if(userProfileResponse.data?.get(0)?.categories!!.contains(",")) {
                    val str =userProfileResponse.data?.get(0)?.categories!!
                    val arr: List<String> = userProfileResponse.data?.get(0)?.categories!!.split(",")
                    val arr21 =ArrayList<String>()

                    for (i in arr.indices){
                            if(arr.get(i).contains("Tattoo")){
                                arr21.add("Tattoo & Piercing")
                            }else if(arr.get(i).contains("Spa")){
                                arr21.add("Spa & Massage")
                            }else if(arr.get(i).contains("Waxing")){
                                arr21.add("Waxing & Hair Removal")
                            }else if(arr.get(i).contains("Nail")){
                                arr21.add("Nail Art & Care")
                            }else{
                                arr21.add(arr.get(i))
                            }
                        }
                    for(i in arr21.indices)
                    {
                        if(!arr21.get(i).equals("")) {
                            uri?.add(arr21.get(i))
                            val chip = Chip(this)
                            chip.text = arr21.get(i)
                            chip.isCloseIconVisible = true
                            chip.setTextColor(Color.WHITE)
                            chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if((i % 4) == 0) R.color.mate_red else if((i % 3) == 0) R.color.mate_pink else R.color.txt_pink)
                            chip.chipCornerRadius = 10.0f
                            chip.setOnCloseIconClickListener(this)
                            atcivityBussinessUpdateBinding?.chipGroup2?.addView(chip)
                        }
                    }
                }
            }
        })



        //getShopDetails
        businessUpdateViewModel!!.getShopTiming(appPreference.USERID.toString()).observe(this, Observer { response ->
            if (response?.data != null) {
                val shopResponse = response.data as ShopItem
                if (!shopResponse.data!!.isEmpty()) {
                    for(i in shopResponse.data.indices)
                    if(shopResponse.data!!.get(i)?.day.equals("0")){
                        if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty() ){
                        atcivityBussinessUpdateBinding?.tvMondayStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvMondayEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbMondayUP?.isChecked=true
                    }else{
                        atcivityBussinessUpdateBinding?.tvMondayStartUP?.setHint("closed")
                        atcivityBussinessUpdateBinding?.tvMondayEndUP?.setHint("closed")

                    }
                    }else if(shopResponse.data!!.get(i)?.day.equals("1")){
                    if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty()){
                    atcivityBussinessUpdateBinding?.tvTuesdayStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvTuesdayEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbTuesdayUP?.isChecked=true
                    }else{
                        atcivityBussinessUpdateBinding?.tvTuesdayStartUP?.setHint("closed")
                        atcivityBussinessUpdateBinding?.tvTuesdayEndUP?.setHint("closed")
                    }
                    }else if(shopResponse.data!!.get(i)?.day.equals("2")){
                        if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty()){

                            atcivityBussinessUpdateBinding?.tvWedStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvWedEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbWednesdayUP?.isChecked=true
                        }else{
                            atcivityBussinessUpdateBinding?.tvWedStartUP?.setHint("closed")
                            atcivityBussinessUpdateBinding?.tvWedEndUP?.setHint("closed")
                        }
                    }else if(shopResponse.data!!.get(i)?.day.equals("3")){
                        if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty()){

                            atcivityBussinessUpdateBinding?.tvThurStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvThurEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbThurdayUP?.isChecked=true}
                        else{
                            atcivityBussinessUpdateBinding?.tvThurStartUP?.setHint("closed")
                            atcivityBussinessUpdateBinding?.tvThurEndUP?.setHint("closed")
                        }
                    }else if(shopResponse.data!!.get(i)?.day.equals("4")){
                        if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty()){

                            atcivityBussinessUpdateBinding?.tvFriStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvFriEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbFridayUP?.isChecked=true}else{
                            atcivityBussinessUpdateBinding?.tvFriStartUP?.setHint("closed")
                            atcivityBussinessUpdateBinding?.tvFriEndUP?.setHint("closed")

                        }
                    }else if(shopResponse.data!!.get(i)?.day.equals("5")){
                        if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty()){

                            atcivityBussinessUpdateBinding?.tvSatStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvSatEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbSaturdayUP?.isChecked=true}
                        else{
                            atcivityBussinessUpdateBinding?.tvSatStartUP?.setHint("closed")
                            atcivityBussinessUpdateBinding?.tvSatEndUP?.setHint("closed")
                        }
                    }else if(shopResponse.data!!.get(i)?.day.equals("6")){
                        if(!shopResponse.data!!.get(i)?.to_time.isNullOrEmpty() && !shopResponse.data!!.get(i)?.from_time.isNullOrEmpty()){

                            atcivityBussinessUpdateBinding?.tvSunStartUP?.setText(shopResponse.data!!.get(i)?.from_time)
                        atcivityBussinessUpdateBinding?.tvSunEndUP?.setText(shopResponse.data!!.get(i)?.to_time)
                        atcivityBussinessUpdateBinding?.cbSundayUP?.isChecked=true}
                        else{
                            atcivityBussinessUpdateBinding?.tvSunStartUP?.setHint("closed")
                            atcivityBussinessUpdateBinding?.tvSunEndUP?.setHint("closed")
                        }
                    }
                }
            }
        })

    }

    private fun uploadClick() {
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

    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems =
            arrayOf("Select from storage", "Capture from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 ->  if (isLegacyExternalStoragePermissionRequired) {
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

    private fun updateClick() {
        var ttime: String? = null

        var timingRequest = TimingRequest()

        var timingItemList = ArrayList<TimingItem>()

        if (cb_mondayUP.isChecked) {

            var timingItem = TimingItem()
            timingItem.day = "monday"
            timingItem.morningTime = tv_monday_startUP.text.toString()
            timingItem.eveningTime = tv_monday_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            var timingItem = TimingItem()
            timingItem.day = "monday"
            timingItem.morningTime = tv_monday_startUP.text.toString()
                timingItem.eveningTime = tv_monday_endUP.text.toString()
                timingItemList.add(timingItem)
                ttime = timingItem.toString()
            }

        if (cb_tuesdayUP.isChecked) {

            var timingItem = TimingItem()
            timingItem.day = "tuesday"
            timingItem.morningTime = tv_tuesday_startUP.text.toString()
            timingItem.eveningTime = tv_tuesday_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()

        } else {
            var timingItem = TimingItem()
            timingItem.day = "tuesday"
            timingItem.morningTime = tv_tuesday_startUP.text.toString()
            timingItem.eveningTime = tv_tuesday_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_wednesdayUP.isChecked) {

            var timingItem = TimingItem()
            timingItem.day = "wednesday"
            timingItem.morningTime = tv_wed_startUP.text.toString()
            timingItem.eveningTime = tv_wed_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            var timingItem = TimingItem()
            timingItem.day = "wednesday"
            timingItem.morningTime = tv_wed_startUP.text.toString()
            timingItem.eveningTime = tv_wed_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }
        if (cb_thurdayUP.isChecked) {


            var timingItem = TimingItem()
            timingItem.day = "thusday"
            timingItem.morningTime = tv_thur_startUP.text.toString()
            timingItem.eveningTime = tv_thur_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            var timingItem = TimingItem()
            timingItem.day = "thusday"
            timingItem.morningTime = tv_thur_startUP.text.toString()
            timingItem.eveningTime = tv_thur_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_fridayUP.isChecked) {


            var timingItem = TimingItem()
            timingItem.day = "friday"
            timingItem.morningTime = tv_fri_startUP.text.toString()
            timingItem.eveningTime = tv_fri_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {

            var timingItem = TimingItem()
            timingItem.day = "friday"
            timingItem.morningTime = tv_fri_startUP.text.toString()
            timingItem.eveningTime = tv_fri_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_saturdayUP.isChecked) {

            val timingItem = TimingItem()
            timingItem.day = "saturday"
            timingItem.morningTime = tv_sat_startUP.text.toString()
            timingItem.eveningTime = tv_sat_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        } else {
            val timingItem = TimingItem()
            timingItem.day = "saturday"
            timingItem.morningTime = tv_sat_startUP.text.toString()
            timingItem.eveningTime = tv_sat_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        if (cb_sundayUP.isChecked) {

            val timingItem = TimingItem()
            timingItem.day = "sunday"
            timingItem.morningTime = tv_sun_startUP.text.toString()
            timingItem.eveningTime = tv_sun_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()

        } else {
            val timingItem = TimingItem()
            timingItem.day = "sunday"
            timingItem.morningTime = tv_sun_startUP.text.toString()
            timingItem.eveningTime = tv_sun_endUP.text.toString()
            timingItemList.add(timingItem)
            ttime = timingItem.toString()
        }

        val gsonBuilder1 = GsonBuilder()

        val gson1: Gson = gsonBuilder1.create()

        timingRequest.shopTime = timingItemList

        val JSONObject: String = gson1.toJson(timingItemList)

     businessUpdateViewModel?.shopTime?.set(JSONObject)

        var servicesof = ""

        if ( atcivityBussinessUpdateBinding!!.rdoMale.isChecked) {
            servicesof = servicesof + "Male"
        }
        if ( atcivityBussinessUpdateBinding!!.rdoFemale.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "female"
        }
        if ( atcivityBussinessUpdateBinding!!.rdoKids.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "kids"
        }
        if ( atcivityBussinessUpdateBinding!!.rdoTrans.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "trans"
        }

        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()

        businessUpdateViewModel?.services?.set(servicesof)

        for (i in 0 until chipGroup2.childCount) {
            val chip = chipGroup2.getChildAt(i) as Chip
            category =
                category + chip.text
                    .toString() + (if (i != chipGroup2.childCount - 1) "," else "")
        }

        businessUpdateViewModel?.categories?.set(category)
        businessUpdateViewModel!!.UpdateBusiness().observe(this,
            { response ->
                if (response?.data != null) {
                    val updateForBusiness = response.data as SignupResponse
                    if (updateForBusiness.message.equals("User updated successfully!")) {
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                    } else {
                            showToast(updateForBusiness!!.message.toString())
                        }
                } else {
                showToast(response.throwable?.message!!)
                }
            })
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

    override fun onBackClick() {

    }

    override fun onUpdateClick() {

    }

    override fun onUploadClick(status: Int) {

    }

    override fun onLocationClick() {

    }

    override fun profileClick(status: Int) {
    }

    override fun onCancelClick() {
        atcivityBussinessUpdateBinding?.consCategoryUP?.visibility = View.GONE
    }

    override fun onConfirmClick() {

    }

    override fun onConfirmLocationClick() {

    }

    override fun onCancelLocationClick() {

    }

    override fun editClick() {

    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {

            R.id.cb_mondayUP -> {
                if (!isChecked) {
                    tv_monday_startUP.setText("")
                    tv_monday_endUP.setText("")
                }
            }
            R.id.cb_tuesdayUP -> {
                if (!isChecked) {
                    tv_tuesday_startUP.setText("")
                    tv_tuesday_endUP.setText("")
                }
            }
            R.id.cb_wednesdayUP -> {
                if (!isChecked) {
                    tv_wed_startUP.setText("")
                    tv_wed_endUP.setText("")
                }
            }

            R.id.cb_thurdayUP -> {
                if (!isChecked) {
                    tv_thur_startUP.setText("")
                    tv_thur_endUP.setText("")
                }
            }
            R.id.cb_fridayUP -> {
                if (!isChecked) {
                    tv_fri_startUP.setText("")
                    tv_fri_endUP.setText("")
                }
            }
            R.id.cb_saturdayUP -> {
                if (!isChecked) {
                    tv_sat_startUP.setText("")
                    tv_sat_endUP.setText("")
                }
            }
            R.id.cb_sundayUP -> {
                if (!isChecked) {
                    tv_sun_startUP.setText("")
                    tv_sun_endUP.setText("")
                }
            }
        }
    }

    private fun ClickListenerforCheckBox() {
        img_monday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (monday == 1) {
                            monday = 0
                            cb_mondayUP.isChecked = false
                            tv_monday_startUP.hint = "Closed"
                            tv_monday_endUP.hint = "Closed"
                            tv_monday_startUP.setText("")
                            tv_monday_endUP.setText("")
                        } else {
                            monday = 1
                            cb_mondayUP.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        img_tuesdayUP.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (tuesday == 1) {
                            tuesday = 0
                            cb_tuesdayUP.isChecked = false
                            tv_tuesday_startUP.hint = "Closed"
                            tv_tuesday_endUP.hint = "Closed"
                            tv_tuesday_startUP.setText("")
                            tv_tuesday_endUP.setText("")
                        } else {
                            tuesday = 1
                            cb_tuesdayUP.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        img_wednesday.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (wednessay == 1) {
                            wednessay = 0
                            cb_wednesdayUP.isChecked = false
                            tv_wed_startUP.hint = "Closed"
                            tv_wed_endUP.hint = "Closed"
                            tv_wed_startUP.setText("")
                            tv_wed_endUP.setText("")
                        } else {
                            wednessay = 1
                            cb_wednesdayUP
                                .isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        img_thurdayUP
            .setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (thusday == 1) {
                            thusday = 0
                            cb_thurdayUP.isChecked = false
                            tv_thur_startUP.hint = "Closed"
                            tv_thur_endUP.hint = "Closed"
                            tv_thur_startUP.setText("")
                            tv_thur_endUP.setText("")
                        } else {
                            thusday = 1
                            cb_thurdayUP.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        img_fridayUP
            .setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (friday == 1) {
                            friday = 0
                            cb_fridayUP.isChecked = false
                            tv_fri_startUP.hint = "Closed"
                            tv_fri_endUP.hint = "Closed"
                            tv_fri_startUP.setText("")
                            tv_fri_endUP.setText("")
                        } else {
                            friday = 1
                            cb_fridayUP.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        img_saturdayUP
            .setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (saturday == 1) {
                            saturday = 0
                            cb_saturdayUP.isChecked = false
                            tv_sat_startUP.hint = "Closed"
                            tv_sat_endUP.hint = "Closed"
                            tv_sat_startUP.setText("")
                            tv_sat_endUP.setText("")
                        } else {
                            saturday = 1
                            cb_saturdayUP.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        img_sundayUP
            .setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (sunday == 1) {
                            sunday = 0
                            cb_sundayUP.isChecked = false
                            tv_sun_startUP.hint = "Closed"
                            tv_sun_endUP.hint = "Closed"
                            tv_sun_startUP.setText("")
                            tv_sun_endUP.setText("")
                        } else {
                            sunday = 1
                            cb_sundayUP.isChecked = true
                        }
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    override fun restoreEasyImageState(): Bundle {
        return easyImageState
    }

    override fun saveEasyImageState(state: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode === RequestCodes.PICK_PICTURE_FROM_GALLERY) {
                easyImage!!.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
                    override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                        lifecycleScope.launch {
                            val file= File(imageFiles[0].file.absolutePath)
                            getUpload(file)
                            fileList.add(FileItem(file))
                            val filess= File(file.absolutePath)
                            if (imageviews==1){
                                businessUpdateViewModel?.upload?.set(filess)
                            }else {
                                businessUpdateViewModel?.profiles?.set(filess)
                            }

                            atcivityBussinessUpdateBinding?.adapter =
                                FilesAdapter(this@BusinessUpdateActivity, fileList, this@BusinessUpdateActivity)
                        }
                        
                    }

                    override fun onImagePickerError(error: Throwable, source: MediaSource) {
                        error.printStackTrace()
                    }

                    override fun onCanceled(source: MediaSource) {
                    }
                })
            } else if (requestCode === RequestCodes.TAKE_PICTURE) {

                easyImage!!.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
                    override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                        lifecycleScope.launch {

                            val file= File(imageFiles[0].file.absolutePath)
                            getUpload(file)
                            img_upload.visibility = View.VISIBLE
                            fileList.add(FileItem(file))
                            val filess= File(file.absolutePath)
                            if (imageviews==1){
                                businessUpdateViewModel?.upload?.set(filess)
                            }else{
                                businessUpdateViewModel?.profiles?.set(filess)
                            }
                            atcivityBussinessUpdateBinding?.adapter =
                                FilesAdapter(this@BusinessUpdateActivity, fileList, this@BusinessUpdateActivity)
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


                try{file = File(path)
                    getUpload(file!!)
                    img_upload.visibility = View.VISIBLE
                    fileList.add(FileItem(file!!))
                    if (imageviews==1){
                        businessUpdateViewModel?.upload?.set(file)
                    }else{
                        businessUpdateViewModel?.profiles?.set(file)
                    }
                    atcivityBussinessUpdateBinding?.adapter =
                        FilesAdapter(this, fileList!!, this)
                }
                catch (e:java.lang.Exception){
                    Toast.makeText(this,""+e, Toast.LENGTH_SHORT)
                }

            }

        }

    }

    override fun onDayStartClick(status: Int) {

        when(status)
        {
            1->{

                setTimeDialog(status, true)

            }

            2->{

                setTimeDialog(status, true)


            }
            3->{
                setTimeDialog(status, true)

            }
            4->{
                setTimeDialog(status, true)
            }

            5->{

                setTimeDialog(status, true)

            }

            6->{

                setTimeDialog(status, true)


            }

            7->{

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
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                setTextField(hourOfDay, minute, status, isFromStart)

            },
            if (isFromStart) 0 else hoursMinute.split(":")[0].toInt()
            ,
            if (isFromStart) 0 else hoursMinute.split(":")[1].toInt(),

            false
        )
        timePickerDialog.show()
    }

    private fun setTextField(hourOfDay: Int, minute: Int, status: Int, isFromStart: Boolean) {
        when (status) {
            1 -> {

                if (isFromStart) {
                    cb_mondayUP.isChecked = true
                    monday=1
                    tv_monday_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_monday_endUP.setText("")

                } else {
                    tv_monday_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    if(!tv_monday_startUP.text.toString().isEmpty() && !tv_monday_endUP.text.toString().isEmpty()){
                        if(!tv_monday_startUP.text.toString().equals(tv_monday_endUP.text.toString())){
                            setAllEdit(tv_monday_startUP.text.toString(),tv_monday_endUP.text.toString())}
                        else{
                            showToast("Select different end time")
                        }
                    }
                }

            }
            2 -> {

                if (isFromStart) {
                    cb_tuesdayUP.isChecked = true
                    tuesday=1
                    tv_tuesday_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_tuesday_endUP.setText("")
                } else {
                    tv_tuesday_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }

            }
            3 -> {
                if (isFromStart) {
                    cb_wednesdayUP.isChecked = true
                    wednessay=1
                    tv_wed_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_wed_endUP.setText("")
                } else {
                    tv_wed_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }


            }
            4 -> {

                if (isFromStart) {
                    cb_thurdayUP.isChecked = true
                    thusday=1
                    tv_thur_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_thur_endUP.setText("")
                } else {
                    tv_thur_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

            5 -> {
                if (isFromStart) {
                    cb_fridayUP.isChecked = true
                    friday=1
                    tv_fri_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_fri_endUP.setText("")
                } else {
                    tv_fri_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

            6 -> {
                if (isFromStart) {
                    cb_saturdayUP.isChecked = true
                    saturday=1
                    tv_sat_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_sat_endUP.setText("")
                } else {
                    tv_sat_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

            7 -> {
                if (isFromStart) {
                    cb_sundayUP.isChecked = true
                    sunday=1
                    tv_sun_startUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                    tv_sun_endUP.setText("")
                } else {
                    tv_sun_endUP.setText(UiUtils.getDate("$hourOfDay:$minute"))
                }
            }

        }

    }

    private fun setAllEdit(date: String, date1: String) {
        atcivityBussinessUpdateBinding?.SetAllField?.visibility=View.VISIBLE
        atcivityBussinessUpdateBinding?.SetAllField?.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {

                tv_tuesday_startUP.setText(tv_monday_startUP .text)
                tv_tuesday_endUP.setText(tv_monday_endUP.text)
                cb_tuesdayUP.isChecked=true
                tuesday=1

                tv_wed_startUP.setText(tv_monday_startUP.text)
                tv_wed_endUP.setText(tv_monday_endUP.text)
                cb_wednesdayUP.isChecked=true
                wednessay=1

                tv_thur_startUP.setText(tv_monday_startUP.text)
                tv_thur_endUP.setText(tv_monday_endUP.text)
                cb_thurdayUP.isChecked=true
                thusday=1

                tv_fri_startUP.setText(tv_monday_startUP.text)
                tv_fri_endUP.setText(tv_monday_endUP.text)
                cb_fridayUP.isChecked=true
                friday=1

                tv_sat_startUP.setText(tv_monday_startUP.text)
                tv_sat_endUP.setText(tv_monday_endUP.text)
                cb_saturdayUP.isChecked=true
                saturday=1

                tv_sun_startUP.setText(tv_monday_startUP.text)
                tv_sun_endUP.setText(tv_monday_endUP.text)
                cb_sundayUP.isChecked=true
                sunday=1

            }

        })
    }

    private fun getMinutesandHours(status: Int): String {

        when (status) {

            1 -> {
                if (TextUtils.isEmpty(tv_monday_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }

                var hours = tv_monday_startUP.text.toString()
                return hours
            }
            2 -> {
                if (TextUtils.isEmpty(tv_tuesday_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_tuesday_startUP.text.toString()
                return hours
            }
            3 -> {
                if (TextUtils.isEmpty(tv_wed_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }

                var hours = tv_wed_startUP.text.toString()
                return hours
            }
            4 -> {
                if (TextUtils.isEmpty(tv_thur_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_thur_startUP.text.toString()
                return hours
            }
            5 -> {

                if (TextUtils.isEmpty(tv_fri_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_fri_startUP.text.toString()
                return hours
            }
            6 -> {
                if (TextUtils.isEmpty(tv_sat_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_sat_startUP.text.toString()
                return hours
            }
            7 -> {
                if (TextUtils.isEmpty(tv_sun_startUP.text.toString())) {
                    showToast(getString(R.string.please_select_start_date))
                    return ""
                }
                var hours = tv_sun_startUP.text.toString()
                return hours
            }
            else -> {
                return ""
            }

        }

    }
    override fun onCategoriesClick() {
        if(ii==0) {
            ii=1
            if (categoryList.size == 0) {
                businessUpdateViewModel!!.getCategory().observe(this, { response ->
                    if (response?.data != null) {
                        val categoryResponse = response.data as CategoryResponse
                        if (categoryResponse.message.equals("categories details")) {
                            cons_categoryUP.visibility = View.VISIBLE
                            categoryList = categoryResponse.data as ArrayList<DataItem>
                            val categoryAdapter =
                                CategoryNewAdapter(this, categoryList, this)
                            rv_categoryUP?.adapter = categoryAdapter
                        } else {
                            showToast(categoryResponse.message!!)
                        }
                    } else {
                        showToast(response.throwable?.message!!)
                    }
                })
            } else {
                cons_categoryUP.visibility = View.VISIBLE

                val categoryAdapter =
                    CategoryNewAdapter(this, categoryList, this)
                rv_category?.adapter = categoryAdapter

            }
        }
        else if(ii==1){
            businessUpdateViewModel?.onCancelClick()
            ii=0
        }


    }

    override fun onDayEndClick(status: Int) {
        setTimeDialog(status, false)
    }

    private fun getUpload(sourceFile: File) {
        if (sourceFile.exists()) {
            val mimeType = if (sourceFile.getAbsolutePath()
                    .contains("jpeg") || sourceFile.getAbsolutePath().contains("jpg")
            ) "image/jpeg" else ""

            if (imageviews == 1) {
                businessUpdateViewModel?.upload?.set(sourceFile)
                businessUpdateViewModel?.uploadmimeType?.set(mimeType)
            }else{
                businessUpdateViewModel?.profiles?.set(sourceFile)
                businessUpdateViewModel?.profilemimeType?.set(mimeType)
            }

        }
    }

    override fun onItemDelterClick(position: Int, fileItem: FileItem) {

    }

    override fun onItemClick(dataItem: DataItem, position: Int) {

        if(position==12){
            atcivityBussinessUpdateBinding?.consCategoryUP?.visibility=View.GONE
            atcivityBussinessUpdateBinding?.CardForEditTextTextPersonName8Others?.visibility=View.VISIBLE
        }
        else{
            if(!chipGroup2.childCount.equals(null)) {
                for (i in 0 until chipGroup2.childCount) {
                    val chip = chipGroup2.getChildAt(i) as Chip
                    if (chip.text.equals(dataItem.name)) {
                        showToast("Already added")
                        return
                    }
                }
            }
            val chip = Chip(this)
            chip.text = dataItem.name.toString()
            chip.setTag(dataItem.id)
            chip.setChipBackgroundColorResource(if ((position % 5) == 0) R.color.txt_orange else if((position % 4) == 0) R.color.mate_red else if((position % 3) == 0) R.color.mate_pink else R.color.txt_pink)
            chip.isCloseIconVisible = true
            chip.setTextColor(Color.WHITE)
            chip.chipCornerRadius = 10.0f
            chip.setOnCloseIconClickListener(this@BusinessUpdateActivity)
            chipGroup2.addView(chip)
        }

    }

    override fun onItemSelectedClick(dataItemList: DataItem, position: Int, isSelected: Boolean,text:String) {
        dataItemList.isSelected = isSelected
        categoryList.get(position).isSelected = isSelected!!
        if (text.equals("Others")) {
            atcivityBussinessUpdateBinding?.consCategoryUP?.visibility=View.GONE
            atcivityBussinessUpdateBinding?.CardForEditTextTextPersonName8Others?.visibility=View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        chipGroup2.removeView(v)
    }

}