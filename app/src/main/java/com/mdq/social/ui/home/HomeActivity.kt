package com.mdq.social.ui.home

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.gowtham.library.utils.LogMessage
import com.gowtham.library.utils.TrimType
import com.gowtham.library.utils.TrimVideo
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.navigationmenu.NevigationMenuItem
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.home.HomeViewModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.app.helper.filecompressor.FileCompressor
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityHomeBinding
import com.mdq.social.ui.addpost.AddpostActivity
import com.mdq.social.ui.bookmark.BookMarkActivity
import com.mdq.social.ui.chat.ChatFragment
import com.mdq.social.ui.login.LoginActivity
import com.mdq.social.ui.models.User
import com.mdq.social.ui.notification.NotificationActivity
import com.mdq.social.ui.privacy.PrivacyActivity
import com.mdq.social.ui.setting.SettingActivity
import com.mdq.social.utils.FileUtils
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import kotlinx.android.synthetic.main.nav_home_fragment.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeNavigator>(), HomeNavigator,
    NavController.OnDestinationChangedListener, NavigationAdapter.NavigationClickManager {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        fun change(fragment:Fragment,fragmentManager:FragmentManager){
            val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.nav_home_fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private var activityHomeBinding: ActivityHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    private var navController: NavController? = null
    private var appBarConfiguration: AppBarConfiguration? = null
    public var slidingRootNav: SlidingRootNav? = null
    private var navigationAdapter: NavigationAdapter? = null
    private var contentURI: Uri? = null
    private var file: File? = null
    var menu_: Menu? = null
    var mArrayUri: ArrayList<String>? = null
    var position = 0
    private val nevigationMenuItemList: ArrayList<NevigationMenuItem> = ArrayList()
    private val menuName = arrayOf("Settings", "Bookmark", "Privacy Policy", "Terms & Conditions")
    private var preferenceManager: PreferenceManager? = null
    private var fragmentManager: FragmentManager? = null

    var draw:Boolean=true
    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun getViewBindingVarible(): Int {
        return BR.homeViewModel
    }

    override fun getViewModel(): BaseViewModel<HomeNavigator> {
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        return homeViewModel!!
    }

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        val deviceId = Secure.getString(
            this.contentResolver,
            Secure.ANDROID_ID
        )

        fragmentManager = supportFragmentManager

        mArrayUri = ArrayList<String>(3)
        activityHomeBinding = getViewDataBinding()
        activityHomeBinding?.homeViewModel = homeViewModel
        homeViewModel?.navigator = this

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val background = resources.getDrawable(R.drawable.bg_gradiant)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.status_bar)
            window.setBackgroundDrawable(background)
        }
        getProfile()

        slidingRootNav = SlidingRootNavBuilder(this)
            .withMenuOpened(false)
             .withMenuLocked(false) //If true, a user can't open or close the menu. Default == false.
            .withContentClickableWhenMenuOpened(true)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()

        textView40.setText(appPreference.NAME.toString())
        navigationAdapter = NavigationAdapter(this, this)
        rv_menu_list.adapter = navigationAdapter

        for (i in menuName.indices) {
            nevigationMenuItemList.add(NevigationMenuItem(menuName[i]))
        }

        navigationAdapter?.setNavigationAdapter(nevigationMenuItemList)
        imageView26.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showToast("Select up to 3 images up to 5MB")
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

        img_menu.setOnClickListener {
            if(draw) {
//                draw=false
                slidingRootNav?.openMenu()

            }else{
                draw=true
                slidingRootNav?.closeMenu()
            }
        }

        img_notification.setOnClickListener {
            startActivity(Intent(this@HomeActivity, NotificationActivity::class.java))
        }
        img_setting.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
        }

        setupNavigationUI()

    }

    private fun getProfile() {
        homeViewModel!!.getUserProfileDetails(
            appPreference.USERID
        ).observe(this) { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileDetailResponse
                if (userProfileResponse != null) {
                    appPreference.TYPE = userProfileResponse.data?.get(0)?.type.toString()
                    appPreference.MOBILENU = userProfileResponse.data?.get(0)?.mobile.toString()

                    if (userProfileResponse.data?.get(0)?.profile_photo != null
                    ) {
                        Glide.with(this).load(
                            "http://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo
                        ).into(imageView10)

                        appPreference.PROFILE =
                            "http://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo

                    }
                }
            }
        }
    }

    private fun showPictureDialog() {

        mArrayUri?.clear()

        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems =
            arrayOf("Gallery", "Video")
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
//                0 -> startActivity(Intent(this, SelectedPostActivity::class.java))
                0 -> Galleries()
//                1 -> takePhotoFromCamera()
                1 -> takeVideoFromCamera()
            }
        }
        pictureDialog.show()
    }


    private fun Galleries() {

        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0)

    }

    private fun takeVideoFromCamera() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            3333
        )
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 2222)
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupNavigationUI() {
        navController = findNavController(R.id.nav_home_fragment)
        navController?.addOnDestinationChangedListener(this)
        appBarConfiguration = AppBarConfiguration(

            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_add,
                R.id.navigation_chat,
                if (appPreference.USERGROUP == "user") {
                    R.id.navigation_user
                } else if (appPreference.USERGROUP == "freelance") {
                    R.id.navigation_freelancer
                } else {
                    R.id.navigation_bussiness
                }
            ), drawer_layout
        )

        nav_bottom.setupWithNavController(navController!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, drawer_layout)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi", "NewApi")
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {

            R.id.navigation_search -> {
                CardForimg_menu.visibility = View.VISIBLE
                CardForimg_notification.visibility = View.INVISIBLE
            }
            R.id.navigation_home -> {
                CardForimg_menu.visibility = View.VISIBLE
                img_notification.setImageResource(R.drawable.ic_home_notification)
                CardForimg_notification.visibility = View.VISIBLE
            }
            R.id.navigation_chat -> {
                CardForimg_menu.visibility = View.GONE
                CardForimg_notification.visibility = View.GONE
            }

            R.id.navigation_bussiness -> {
                img_back.visibility = View.INVISIBLE
                CardForimg_notification.visibility = View.GONE
                CardForimg_menu.visibility = View.VISIBLE
            }

            R.id.navigation_search_details -> {
                CardForimg_notification.visibility = View.VISIBLE
                CardForimg_menu.visibility = View.INVISIBLE
                img_notification.setImageResource(R.drawable.ic_filter)
            }

            else -> {
                img_menu.setBackgroundResource(R.drawable.bg_white_corners)
                img_notification.setBackgroundResource(R.drawable.bg_white_corners)
                img_menu.visibility = View.VISIBLE
                img_notification.visibility = View.VISIBLE
                img_setting.visibility = View.GONE
                img_back.visibility = View.GONE
                tv_title.visibility = View.GONE
                img_notification.setImageResource(R.drawable.ic_home_notification)
            }
        }
    }

    override fun onClick(pos: Int) {
        if (pos == 0) {
//            slidingRootNav?.openMenu()
            startActivity(SettingActivity.getCallingIntent(this))
        } else if (pos == 1) {
            val i = Intent(applicationContext, BookMarkActivity::class.java)
            startActivity(i)
        } else if (pos == 2) {
//            startActivity(ProfileActivity.getCallingIntent(this))
            startActivity(PrivacyActivity.getCallingIntent(this))

        } else if (pos == 3) {
        } else if (pos == 4) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode === 2222) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val thumbnail = data!!.extras!!["data"] as Bitmap?
                    val tempUri: Uri = getImageUri(applicationContext, thumbnail!!)
                    var file: File? = File(FileUtils.getPath(applicationContext, tempUri))

                    startActivity(
                        Intent(this, AddpostActivity::class.java).putExtra(
                            AppConstants.IMAGE,
                            file?.absolutePath
                        ).putExtra("Bitmap", tempUri.toString())
                    )
                } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    val thumbnail = data!!.extras!!["data"] as Bitmap?

                    file = bitmapToFile(this, thumbnail!!, System.currentTimeMillis().toString())
                    val tempUri: Uri = getImageUri(applicationContext, thumbnail!!)
                    val fileCompressor = FileCompressor()
                    val imageFile = fileCompressor.compressImage(file?.absolutePath, this)
                    val doc = File(imageFile)

                    startActivity(
                        Intent(this, AddpostActivity::class.java).putExtra(
                            AppConstants.IMAGE,
                            doc.absolutePath
                        ).putExtra("Bitmap", tempUri.toString())
                    )
                }
            } else if (requestCode == 3333) {

                contentURI = data?.data
                val returnUri1 = data!!.data
                var fileDescriptor: AssetFileDescriptor? = null
                try {
                    fileDescriptor = applicationContext.contentResolver.openAssetFileDescriptor(
                        returnUri1!!,
                        "r"
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

                val fileSize = fileDescriptor!!.length
                if (fileSize < 10009999) {
                    val startForResult =
                        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                            if (result.resultCode == Activity.RESULT_OK &&
                                result.getData() != null
                            ) {
                                var uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()))
                                val filePath: String =
                                    FileUtils.getPath(this, Uri.parse(contentURI.toString()))
                                file = File(filePath)

                                startActivity(
                                    Intent(
                                        this,
                                        AddpostActivity::class.java
                                    )
                                        .putExtra("uri", contentURI.toString())
                                        .putExtra(AppConstants.VIDEOS, file?.absolutePath)
                                )
                                Log.d("path", "Trimmed path:: " + uri)
                            } else
                                LogMessage.v("videoTrimResultLauncher data is null")
                        }

                    TrimVideo.activity(contentURI.toString())
                        .setTrimType(TrimType.MIN_MAX_DURATION)
                        .setMinToMax(5, 30)  //seconds
//                        .setCompressOption( CompressOption()) //empty constructor for default compress option
                        .setHideSeekBar(false)
                        .start(this, startForResult)

                } else {
                    showToast("Select less than 10MB video")
                }
            } else if (requestCode == 0) {
                if (requestCode === 0 && resultCode === RESULT_OK && null != attr.data) {
                    if (data?.getClipData() != null) {
                        mArrayUri?.clear()
                        val mClipData: ClipData? = data?.getClipData()
                        val cout: Int? = data?.getClipData()?.getItemCount()
                        if (cout != null) {
                            if (cout >= 4) {
                                showToast("Select less than 4 images")
                            } else {
                                for (i in 0 until cout!!) {
                                    val imageurl: Uri? = data?.getClipData()?.getItemAt(i)?.getUri()
                                    if (imageurl != null) {
                                        mArrayUri!!.add(imageurl.toString())
                                    }
                                }
                                if (mArrayUri != null) {
                                    Log.i("enterSizeafter", mArrayUri!!.size.toString())

                                    startActivity(
                                        Intent(this@HomeActivity, AddpostActivity::class.java)
                                            .putStringArrayListExtra("AAA", mArrayUri)
                                    )
                                }
                            }
                        }
                    } else {
                        val imageurl: Uri? = data?.getData()
                        mArrayUri?.add(imageurl!!.toString())
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                AddpostActivity::class.java
                            ).putStringArrayListExtra("AAA", mArrayUri)
                        )
                    }
                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val changeImage = findViewById<View>(R.id.navigation_search) as MenuItem
        changeImage.setIcon(R.drawable.homes)
        return super.onOptionsItemSelected(item)
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        var path: String? = null
        if (inContext != null) {
            path =
                MediaStore.Images.Media.insertImage(
                    inContext.contentResolver,
                    inImage,
                    "title",
                    null
                )
        }
        return Uri.parse(path)
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(applicationContext)
        }
        return preferenceManager
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.nav_home_fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}