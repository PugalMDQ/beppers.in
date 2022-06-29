package com.mdq.social.ui.selectedpost

import android.Manifest
import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.selectpost.SelectPostViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivitySelectImageBinding
import com.mdq.social.ui.addpost.AddpostActivity
import kotlinx.android.synthetic.main.activity_add_post.*
import java.util.*
import kotlin.collections.ArrayList

class SelectedPostActivity : BaseActivity<ActivitySelectImageBinding, SelectedPostNavigator>(),
    SelectedPostNavigator, SelectPostAdapter.ClickManager {

    private var selectPostViewModel: SelectPostViewModel? = null
    private var activitySelectImageBinding: ActivitySelectImageBinding? = null

    private var imgurls: ArrayList<SelectedPostItem>? = null

    private var selectedImage: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_select_image
    }

    override fun getViewBindingVarible(): Int {
        return BR.profileViewModel
    }

    override fun getViewModel(): BaseViewModel<SelectedPostNavigator> {
        selectPostViewModel =
            ViewModelProvider(this, viewModelFactory).get(SelectPostViewModel::class.java)
        return selectPostViewModel!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySelectImageBinding = getViewDataBinding()


        activitySelectImageBinding?.imageView?.setOnClickListener {
            finish()
        }
        activitySelectImageBinding?.tvNext?.setOnClickListener {

            if(!selectedImage.isNullOrEmpty()) {
                startActivity(
                    Intent(this, AddpostActivity::class.java).putExtra(
                        AppConstants.IMAGE,
                        selectedImage
                    )
                )
            }
        }


        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showToast(getString(R.string.all_permissions_granted))

                        populateImagesFromGallery()
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

    private fun loadPhotosFromNativeGallery(): ArrayList<SelectedPostItem>? {
        val columns =
            arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN
        val imagecursor = managedQuery(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
            null, "$orderBy DESC"
        )
        var imageUrls = ArrayList<SelectedPostItem>()
        for (i in 0 until imagecursor.count) {
            imagecursor.moveToPosition(i)
            val dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA)
            imageUrls.add(SelectedPostItem(imagecursor.getString(dataColumnIndex), false))
            println("=====> Array path => " + imageUrls[i])
        }
        return imageUrls
    }

    private fun mayRequestGalleryImages(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(permission.READ_EXTERNAL_STORAGE)) {
            showPermissionRationaleSnackBar()
        } else {
            requestPermissions(
                arrayOf(permission.READ_EXTERNAL_STORAGE),
                1111
            )
        }
        return false
    }

    private fun showPermissionRationaleSnackBar() {
        Snackbar.make(
            findViewById(R.id.tv_next),
            "Storage permission is needed for fetching images from Gallery.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("OK") { // Request the permission
            ActivityCompat.requestPermissions(
                this@SelectedPostActivity,
                arrayOf(permission.READ_EXTERNAL_STORAGE),
                1111
            )
        }.show()
    }

    private fun populateImagesFromGallery() {
        if (!mayRequestGalleryImages()) {
            return
        }
        imgurls = loadPhotosFromNativeGallery()
        initializeRecyclerView(imgurls)
    }

    private fun initializeRecyclerView(imageUrls: ArrayList<SelectedPostItem>?) {
        activitySelectImageBinding?.adapter = SelectPostAdapter(imageUrls, this, this)

    }

    override fun onItemClick(position: Int, selectedPostItem: SelectedPostItem) {

        selectedImage = selectedPostItem.imgPath

        Glide.with(this).load("file://" + selectedPostItem.imgPath)
            .into(activitySelectImageBinding?.imageView46!!)

    }


}