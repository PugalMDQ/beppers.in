package com.mdq.social.ui.freelancer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fasttrack.attachment.helper.upload.*
import com.fasttrack.attachment.helper.upload.RequestCodes
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.R
import com.mdq.social.BR
import com.mdq.social.app.data.response.common.ResponseStatus
import com.mdq.social.app.data.response.profileupdate.ProfileUpdateResponse
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.freelancer.FreelancerViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentFreelancerBinding
import com.mdq.social.ui.business.MenuAdapter
import com.mdq.social.ui.freelanceupdate.FreelanceUpdateActivity
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import kotlinx.android.synthetic.main.fragment_freelancer.*
import kotlinx.coroutines.launch
import java.io.File

class FreelancerFragment : BaseFragment<FragmentFreelancerBinding, FreelancerProfileNavigator>(),
    FreelancerProfileNavigator,EasyImage.EasyImageStateHandler {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpBusinessActivity::class.java)
        }
        const val IMAGE_PICK_CODE = 999
        const val LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 456
        val LEGACY_WRITE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private var freelancerViewModel: FreelancerViewModel? = null
    private var fragmentFreelancerBinding: FragmentFreelancerBinding? = null
    private var easyImage: EasyImage? = null
    private val easyImageState = Bundle()

    lateinit var adapters:MenuAdapter
     var listItems:ArrayList<Uri> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_freelancer
    }

    override fun getViewModel(): BaseViewModel<FreelancerProfileNavigator> {
        freelancerViewModel =
            ViewModelProvider(this, viewModelFactory).get(FreelancerViewModel::class.java)
        return freelancerViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.homeViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFreelancerBinding = getViewDataBinding()

        fragmentFreelancerBinding?.edit?.setOnClickListener {
            startActivity(Intent(requireContext(),FreelanceUpdateActivity::class.java))
        }

        easyImage = EasyImage.Builder(requireContext())
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(true)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Makeovers")
                .allowMultiple(true)
                .setStateHandler(this)
                .build()

        adds.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,232)
        }
        getProfile()

    }

    private fun getProfile() {

        freelancerViewModel!!.getUserProfile(
            appPreference.USERID
        ).observe(requireActivity(), Observer { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileResponse

            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun profileClick() {
        Dexter.withContext(requireContext())
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
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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


    override fun restoreEasyImageState(): Bundle {
        return easyImageState
    }

    override fun saveEasyImageState(state: Bundle?) {
    }


    private val isLegacyExternalStoragePermissionRequired: Boolean
        private get() {
            val permissionGranted = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            return Build.VERSION.SDK_INT < 29 && !permissionGranted
        }

    private fun requestLegacyWriteExternalStoragePermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                LEGACY_WRITE_PERMISSIONS,
                LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode === RequestCodes.PICK_PICTURE_FROM_GALLERY) {

                easyImage!!.handleActivityResult(
                        requestCode,
                        resultCode,
                        data,
                        requireActivity(),
                        object : DefaultCallback() {
                            override fun onMediaFilesPicked(
                                    imageFiles: Array<MediaFile>,
                                    source: MediaSource
                            ) {
                                lifecycleScope.launch {
                                    val file = File(imageFiles[0].file.absolutePath)
                                    getUpload(file)
                                    val filess = File(file.absolutePath)
                                    freelancerViewModel?.upload?.set(filess)
                                    profileUpdate()
                                }
                            }

                            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                                error.printStackTrace()
                            }

                            override fun onCanceled(source: MediaSource) {
                            }
                        })
            }
            else if (requestCode === RequestCodes.TAKE_PICTURE) {

                easyImage!!.handleActivityResult(
                        requestCode,
                        resultCode,
                        data,
                        requireActivity(),
                        object : DefaultCallback() {
                            override fun onMediaFilesPicked(
                                    imageFiles: Array<MediaFile>,
                                    source: MediaSource
                            ) {
                                lifecycleScope.launch {

                                    val file = File(imageFiles[0].file.absolutePath)
                                    getUpload(file)
                                    val filess = File(file.absolutePath)
                                    freelancerViewModel?.upload?.set(filess)
                                    profileUpdate()
                                }
                            }

                            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                                error.printStackTrace()
                            }

                            override fun onCanceled(source: MediaSource) {
                            }
                        })
            }
            else if(requestCode==232){
                var ImageUri:Uri= data?.data!!
                listItems.add(ImageUri)
            }
        }
    }

    private fun profileUpdate() {
        freelancerViewModel?.updateProfile()?.observe(this, Observer { response ->
            if (response?.data != null) {
                var profileUpdateResponse = response.data as ProfileUpdateResponse
                if (profileUpdateResponse != null && profileUpdateResponse.status == ResponseStatus.SUCCESS) {
                    showToast(profileUpdateResponse.message)
                } else {
                    showToast(profileUpdateResponse.message!!)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun getUpload(sourceFile: File) {
        if (sourceFile.exists()) {
            val mimeType = if (sourceFile.getAbsolutePath()
                            .contains("jpeg") || sourceFile.getAbsolutePath().contains("jpg")
            ) "image/jpeg" else ""

            freelancerViewModel?.upload?.set(sourceFile)
            freelancerViewModel?.uploadmimeType?.set(mimeType)

            val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_logo)
            Glide.with(this)
                    .load(sourceFile)
                    .apply(options)
                    .into(imageView19)
        }
    }
}