package com.mdq.social.ui.freelanceupdate

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
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
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.freelanceupdate.FreelanceUpdateViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityFreelanceUpdateBinding
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import com.mdq.social.ui.signupfreelancer.CategoryAdapter
import com.mdq.social.ui.signupfreelancer.CategoryNewAdapter
import com.mdq.social.ui.signupfreelancer.FileItem
import com.mdq.social.ui.signupfreelancer.FilesAdapter
import com.mdq.social.utils.FileUtils
import kotlinx.android.synthetic.main.activity_bussiness_update.*
import kotlinx.android.synthetic.main.activity_freelance_update.*
import kotlinx.android.synthetic.main.activity_freelance_update.cons_category
import kotlinx.android.synthetic.main.activity_freelance_update.rv_category
import kotlinx.android.synthetic.main.activity_signup_freelancer.*
import kotlinx.android.synthetic.main.activity_signup_freelancer.imageView22
import kotlinx.android.synthetic.main.item_for_toast.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FreelanceUpdateActivity:BaseActivity<ActivityFreelanceUpdateBinding,
        FreelanceUpdateNavigator>(),
    CategoryAdapter.ClickManager, FilesAdapter.ClickManager, CategoryNewAdapter.ClickManager,
    EasyImage.EasyImageStateHandler,
    FreelanceUpdateNavigator, View.OnClickListener {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, FreelanceUpdateActivity::class.java)
        }
    }

    private var activityFreelanceUpdateBinding: ActivityFreelanceUpdateBinding? = null
    private var freelanceUpdateViewModel: FreelanceUpdateViewModel? = null
    private var categoryList = java.util.ArrayList<DataItem>()
    var category = ""
    private var ii:Int = 0
    private var easyImage: EasyImage? = null
    private val easyImageState = Bundle()
    private var fileList = ArrayList<FileItem>()
    private var file: File? = null
    private var imageviews: Int? = 0
    private var contentURI: Uri? = null
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private var latt:Double = 0.0
    private var lonn:Double = 0.0
    override fun getLayoutId(): Int {
        return R.layout.activity_freelance_update
    }

    override fun getViewBindingVarible(): Int {
        return BR.freelanceUpdateViewModel
    }


    override fun getViewModel(): BaseViewModel<FreelanceUpdateNavigator> {
        freelanceUpdateViewModel =
            ViewModelProvider(this, viewModelFactory).get(FreelanceUpdateViewModel::class.java)
        return freelanceUpdateViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityFreelanceUpdateBinding = getViewDataBinding()
        activityFreelanceUpdateBinding?.freelanceUpdateViewModel = freelanceUpdateViewModel
        freelanceUpdateViewModel?.navigator = this

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        activityFreelanceUpdateBinding?.rdoTrevelYes?.setOnClickListener {
            freelanceUpdateViewModel?.travel?.set("Travel other cities")
        }

        activityFreelanceUpdateBinding?.rdoTrevelNo?.setOnClickListener {
            freelanceUpdateViewModel!!.travel.set("Can't travel other cities")
        }

        easyImage = EasyImage.Builder(this)
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(true)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("Makeovers")
            .allowMultiple(true)
            .setStateHandler(this)
            .build()

        activityFreelanceUpdateBinding?.address?.setOnClickListener {
            onLocationClicks()
        }
        activityFreelanceUpdateBinding?.textView51?.setOnClickListener {
            try {
                profileClick()
            }catch (e:Exception){
            }
        }

        getProfileDetail()

        activityFreelanceUpdateBinding!!.textView2.setOnClickListener {
            updateClicks()
        }
        activityFreelanceUpdateBinding!!.nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                activityFreelanceUpdateBinding!!.imageView.visibility = View.INVISIBLE
            } else {
                activityFreelanceUpdateBinding!!.imageView.visibility = View.VISIBLE
            }
        })

        activityFreelanceUpdateBinding!!.addCategory.setOnClickListener {

            if (!activityFreelanceUpdateBinding!!.editTextTextPersonName8Others.text.isEmpty()) {
                val chip = Chip(this)
                chip.text =
                    activityFreelanceUpdateBinding!!.editTextTextPersonName8Others.text.toString()
                        .trim()+","
                chip.isCloseIconVisible = true
                chip.setTextColor(Color.WHITE)
                chip.chipCornerRadius = 10.0f
                chip.setChipBackgroundColorResource(R.color.txt_orange )
                chip.setOnCloseIconClickListener(this)
                chipGroupF.addView(chip)
                chipGroupF.visibility = View.VISIBLE
                activityFreelanceUpdateBinding!!.editTextTextPersonName8Others.setText("")
            }
        }
    }

    private fun updateClicks() {
        var servicesof = ""

        if ( activityFreelanceUpdateBinding!!.rdoMale.isChecked) {
            servicesof = servicesof + "Male"
        }
        if ( activityFreelanceUpdateBinding!!.rdoFemale.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "female"
        }
        if ( activityFreelanceUpdateBinding!!.rdoKids.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "kids"
        }
        if ( activityFreelanceUpdateBinding!!.rdoTrans.isChecked) {
            servicesof = servicesof + (if (!servicesof.isNullOrEmpty()) "," else "") + "trans"
        }

        val gsonBuilder = GsonBuilder()

        val gson: Gson = gsonBuilder.create()

        freelanceUpdateViewModel?.services?.set(servicesof)

        for (i in 0 until chipGroupF.childCount) {
            val chip = chipGroupF.getChildAt(i) as Chip
            category =
                category + chip.text
                    .toString() + (if (i != chipGroupF.childCount - 1) "," else "")
        }

        freelanceUpdateViewModel?.categories?.set(category)

        freelanceUpdateViewModel!!.UpdateFreelancer().observe(this,
            { response ->
                if (response?.data != null) {
                    val updateForBusiness = response.data as SignupResponse
                    if (updateForBusiness.message.equals("User updated successfully!")) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        showToast(updateForBusiness!!.message.toString())
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
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
                                            this@FreelanceUpdateActivity,
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
                        activityFreelanceUpdateBinding?.editTextTextPersonName19?.setText(add)
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
                            val geocoder = Geocoder(this@FreelanceUpdateActivity, Locale.getDefault())
                            var addresses: List<Address>? = null
                            try {
                                addresses = geocoder.getFromLocation(
                                    location1.latitude,
                                    location1.longitude,
                                    1
                                )
                                val add = (addresses as MutableList<Address>?)?.get(0)?.getAddressLine(0)
                                activityFreelanceUpdateBinding?.editTextTextPersonName19?.setText(add)
                                freelanceUpdateViewModel?.city?.set(activityFreelanceUpdateBinding?.editTextTextPersonName6?.text.toString())

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
        freelanceUpdateViewModel!!.getUserProfileDetails(appPreference.USERID.toString()).observe(this, Observer { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileDetailResponse
                activityFreelanceUpdateBinding?.editTextTextPersonName50?.setText(userProfileResponse?.data?.get(0)?.user_name)
                activityFreelanceUpdateBinding?.editTextTextPersonName19?.setText(userProfileResponse?.data?.get(0)?.address)
                activityFreelanceUpdateBinding?.editTextTextPersonName5?.setText(userProfileResponse?.data?.get(0)?.password)
                activityFreelanceUpdateBinding?.editTextTextPersonName2?.setText(userProfileResponse?.data?.get(0)?.email)
                activityFreelanceUpdateBinding?.editTextTextPersonName7?.setText(userProfileResponse?.data?.get(0)?.mobile)
                activityFreelanceUpdateBinding?.editTextTextPersonName?.setText(userProfileResponse?.data?.get(0)?.name)
                activityFreelanceUpdateBinding?.editTextTextPersonName18?.setText(userProfileResponse?.data?.get(0)?.description)

                if(userProfileResponse.data?.get(0)?.service?.contains("Male")!!){
                    activityFreelanceUpdateBinding?.rdoMale?.isChecked=true
                }
                if(userProfileResponse.data?.get(0)?.service?.contains("female")!!){
                    activityFreelanceUpdateBinding?.rdoFemale?.isChecked=true
                }
                if(userProfileResponse.data?.get(0)?.service?.contains("kids")!!){
                    activityFreelanceUpdateBinding?.rdoKids?.isChecked=true
                }
                if(userProfileResponse.data?.get(0)?.service?.contains("trans")!!){
                    activityFreelanceUpdateBinding?.rdoTrans?.isChecked=true
                }


                if(userProfileResponse.data.get(0)?.travel!!.trim().contains("Travel other cities")){

                    activityFreelanceUpdateBinding?.rdoTrevelYes?.isChecked=true
                    freelanceUpdateViewModel?.travel?.set(userProfileResponse.data.get(0)?.travel)
                }else{
                    activityFreelanceUpdateBinding?.rdoTrevelNo?.isClickable=true
                    freelanceUpdateViewModel?.travel?.set(userProfileResponse.data.get(0)?.travel)
                }

                var uri: ArrayList<String>?= ArrayList()
                if(userProfileResponse.data?.get(0)?.categories!!.contains(",")) {
                    val str =userProfileResponse.data?.get(0)?.categories!!
                    val arr: List<String> = userProfileResponse.data?.get(0)?.categories!!.split(",")
                    for(i in arr.indices)
                    {
                        if(!arr.get(i).equals("")) {
                            uri?.add(arr.get(i))
                            val chip = Chip(this)
                            chip.text = arr.get(i)
                            chip.isCloseIconVisible = true
                            chip.setTextColor(Color.WHITE)
                            chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if((i % 4) == 0) R.color.mate_red else if((i % 3) == 0) R.color.mate_pink else R.color.txt_pink)
                            chip.chipCornerRadius = 10.0f
                            chip.setOnCloseIconClickListener(this)
                            activityFreelanceUpdateBinding?.chipGroupF?.addView(chip)
                        }
                    }
                }
            }
        })
    }

    override fun onBackClick() {
        finish()
    }

    override fun updateClick() {

    }

    override fun onCategoryClick() {
        if(ii==0) {
            ii=1
            if (categoryList.size == 0) {
                freelanceUpdateViewModel!!.getCategory().observe(this, { response ->
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
            freelanceUpdateViewModel?.onCancelClick()
            ii=0
        }
    }

    override fun onLocationClick() {

    }


    private fun getUpload(sourceFile: File) {
        if (sourceFile.exists()) {
            val mimeType = if (sourceFile.getAbsolutePath()
                    .contains("jpeg") || sourceFile.getAbsolutePath().contains("jpg")
            ) "image/jpeg" else ""

            freelanceUpdateViewModel?.upload?.set(sourceFile)
            freelanceUpdateViewModel?.uploadmimeType?.set(mimeType)
        }
    }

    private fun profileClick() {
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
                chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if((i % 4) == 0) R.color.mate_red else if((i % 3) == 0) R.color.mate_pink else R.color.txt_pink)
                chip.chipCornerRadius = 10.0f
                chip.setOnCloseIconClickListener(this)
                chipGroup.addView(chip)

            }

        }


    }

    override fun onCancelLocationClick() {
    }

    override fun onConfirmLocationClick() {
    }

    override fun editClick() {

    }


    override fun onItemClick(dataItem: DataItem, position: Int) {
        if(position==12){
            activityFreelanceUpdateBinding?.consCategory?.visibility=View.GONE
            activityFreelanceUpdateBinding?.CardForEditTextTextPersonName8Others?.visibility=View.VISIBLE
        }else {
            for (i in 0 until chipGroupF.childCount) {
                val chip = chipGroupF.getChildAt(i) as Chip
                if (chip.text.equals(dataItem.name)) {
                    showToast("Already added")
                    return
                }
            }

            val chip = Chip(this)
            chip.text = dataItem.name.toString()
            chip.setTag(dataItem.id)
            chip.setTag(dataItem.id)
            chip.setChipBackgroundColorResource(if ((position % 5) == 0) R.color.txt_orange else if((position % 4) == 0) R.color.mate_red else if((position % 3) == 0) R.color.mate_pink else R.color.txt_pink)
            chip.isCloseIconVisible = true
            chip.setTextColor(Color.WHITE)
            chip.chipCornerRadius = 10.0f
            chip.setOnCloseIconClickListener(this)
            chipGroupF.addView(chip)

        }

    }
    override fun onItemSelectedClick(dataItemList: DataItem, position: Int, isSelected: Boolean,text:String) {
        dataItemList.isSelected = isSelected
        categoryList.get(position).isSelected=isSelected!!
        if (text.equals("Others")) {
            activityFreelanceUpdateBinding?.consCategory?.visibility=View.GONE
            activityFreelanceUpdateBinding?.CardForEditTextTextPersonName8Others?.visibility=View.VISIBLE
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
                    try {
                        easyImage!!.openGallery(this@FreelanceUpdateActivity)
                    }catch (e:Exception){

                    }
                }
                1 ->  if (isLegacyExternalStoragePermissionRequired) {
                    requestLegacyWriteExternalStoragePermission()
                } else {
                    try{
                        easyImage!!.openCameraForImage(this@FreelanceUpdateActivity)
                    }catch (e:Exception){

                    }
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
        chipGroupF.removeView(v)
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
                                    img_uploadF.visibility = View.VISIBLE
                                    fileList.add(FileItem(file!!))
                                    freelanceUpdateViewModel?.upload?.set(file!!)
                                    activityFreelanceUpdateBinding?.adapter =
                                        FilesAdapter(
                                            this@FreelanceUpdateActivity,
                                            fileList!!,
                                            this@FreelanceUpdateActivity
                                        )

                                } else {
                                    val options: RequestOptions = RequestOptions()
                                        .placeholder(R.drawable.ic_placeholderimage)
                                        .error(R.drawable.ic_placeholderimage)
                                        .apply(RequestOptions.signatureOf(ObjectKey(System.currentTimeMillis())))
                                        .transform(RoundedCorners(25))
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    Glide.with(this@FreelanceUpdateActivity)
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
                                    img_uploadF.visibility = View.VISIBLE
                                    fileList.clear()
                                    fileList.add(FileItem(files))
                                    freelanceUpdateViewModel?.upload?.set(files!!)
                                    activityFreelanceUpdateBinding?.adapter = FilesAdapter(
                                        this@FreelanceUpdateActivity,
                                        fileList!!,
                                        this@FreelanceUpdateActivity
                                    )
                                } else {
                                    val options: RequestOptions = RequestOptions()
                                        .placeholder(R.drawable.ic_placeholderimage)
                                        .error(R.drawable.ic_placeholderimage)
                                        .apply(RequestOptions.signatureOf(ObjectKey(System.currentTimeMillis())))
                                        .transform(RoundedCorners(25))
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    Glide.with(this@FreelanceUpdateActivity)
                                        .load(file?.absolutePath)
                                        .apply(options)
                                        .into(imageView22)
                                }

                                val file = File(imageFiles[0].file.absolutePath)
                                getUpload(file)
                                img_uploadF.visibility = View.VISIBLE

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
                img_uploadF.visibility = View.GONE

                val path =
                    FileUtils.getPath(this, contentURI)

                file = File(path)
                fileList.clear()
                fileList.add(FileItem(file!!))
                freelanceUpdateViewModel?.upload?.set(file!!)

                activityFreelanceUpdateBinding?.adapter = FilesAdapter(this, fileList!!, this)

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

    override fun onItemDelterClick(position: Int, fileItem: FileItem) {

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

}