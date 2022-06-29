package com.mdq.social.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fasttrack.attachment.helper.upload.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.common.ResponseStatus
import com.mdq.social.app.data.response.profileupdate.ProfileUpdateResponse
import com.mdq.social.app.data.response.user_profile.DataItem
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.profile.ProfileViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentProfileBinding
import com.mdq.social.ui.individual.IndividualActivity
import com.mdq.social.ui.reviewlist.ReviewListActivity
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileNavigator>(),
        ProfileNavigator, EasyImage.EasyImageStateHandler,ProfileAdapter.ClickManager {
    companion object {
        const val IMAGE_PICK_CODE = 999
        const val LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 456
        val LEGACY_WRITE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private var profileViewModel: ProfileViewModel? = null
    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private var profileAdapter: ProfileAdapter? = null
    private var userId: Int? = -1
    private var dialog: Dialog? = null
    private var easyImage: EasyImage? = null
    private val easyImageState = Bundle()
    private var statuss: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun getViewModel(): BaseViewModel<ProfileNavigator> {
        profileViewModel =
                ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        return profileViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.profileViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt(AppConstants.USER_ID, -1)

        lin_mess.setOnClickListener {
            showToast("Get 5000 follower to enable this")
        }

        fragmentProfileBinding?.edit?.setOnClickListener {
            startActivity(Intent(requireActivity(),IndividualActivity::class.java))
        }
        if (userId != null) {
            if (!userId?.toString().equals(appPreference.USERID)) {

                blin_mess.visibility = View.VISIBLE
                flin_mess.visibility = View.INVISIBLE

            } else {

                blin_mess.visibility = View.VISIBLE
                flin_mess.visibility = View.INVISIBLE
            }

        } else {

            lin_mess.visibility = View.VISIBLE
            blin_mess.visibility = View.VISIBLE
            flin_mess.visibility = View.INVISIBLE

        }

        fragmentProfileBinding = getViewDataBinding()
        fragmentProfileBinding?.profileViewModel = profileViewModel
        profileViewModel?.navigator = this

        if (appPreference.USERGROUP == "1") {
            cv_user.visibility = View.VISIBLE
            sc_buss.visibility = View.GONE
            sc_free.visibility = View.GONE
        } else if (appPreference.USERGROUP == "2") {
            sc_buss.visibility = View.VISIBLE
            sc_free.visibility = View.GONE
            cv_user.visibility = View.GONE

        } else {
            cv_user.visibility = View.GONE
            sc_buss.visibility = View.GONE
            sc_free.visibility = View.VISIBLE

        }
        easyImage = EasyImage.Builder(requireContext())
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(true)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Makeovers")
                .allowMultiple(true)
                .setStateHandler(this)
                .build()

        getProfile()


        tv_add_rating_1.setOnClickListener {
            setDialogReview()
        }

        tv_add_rating_2.setOnClickListener {
            setDialogReview()
        }

        textView51.setOnClickListener {
            updateReview(
                    "0",
                    "",
                    userId.toString(),
                    ""
            )
        }

        tvfollow.setOnClickListener {
            updateReview(
                    "0",
                    "",
                    userId.toString(),
                    ""
            )
        }

        ftextView51.setOnClickListener {
            updateReview(
                    "0",
                    "",
                    userId.toString(),
                    ""
            )
        }

        fragmentProfileBinding?.ftextView126?.setOnClickListener {
            if (userId != null) {
                if (!userId?.toString().equals(appPreference.USERID)) {
                    setDialogReview()

                }
            }
        }

        fragmentProfileBinding?.textView126?.setOnClickListener {
            if (userId != null) {
                if (!userId?.toString().equals(appPreference.USERID)) {
                    setDialogReview()

                }
            }
        }

    }


    private fun setDialogReview() {
        dialog = Dialog(requireActivity(), R.style.dialog_center)
        dialog?.setContentView(R.layout.dialog_review)

        var tvSubmit = dialog?.findViewById<TextView>(R.id.tv_submit)
        var rating = dialog?.findViewById<RatingBar>(R.id.rating)
        var tvDescription = dialog?.findViewById<TextView>(R.id.edt_deacription)

        tvSubmit?.setOnClickListener {

            if (rating?.rating == 0.0F) {
                showToast("Please Update rating")
                return@setOnClickListener
            }

            if (tvDescription?.text.toString().isNullOrEmpty()) {
                showToast("Please enter description")
                return@setOnClickListener
            }
            updateReview(
                    "0",
                    rating?.rating.toString(),
                    userId.toString(),
                    tvDescription?.text.toString()
            )
        }

        dialog?.show()

    }

    private fun getProfile() {

        profileViewModel!!.getUserProfile(
                if (userId == null || userId == -1) appPreference.USERID else userId.toString()
        ).observe(requireActivity(), Observer { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileResponse

                profileAdapter= context?.let { ProfileAdapter(it,userProfileResponse.data as List<DataItem>,this) }
                fragmentProfileBinding?.gal?.adapter=profileAdapter
                val options: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_no_pictures)
                        .error(R.drawable.ic_no_pictures)
                Glide.with(this)
                        .load("http://mdqualityapps.in/gallery/"+userProfileResponse.data?.get(0)?.gallery)
                        .apply(options)
                        .into(bimageView19)

                val optionsb: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_no_pictures)
                        .error(R.drawable.ic_no_pictures)
                Glide.with(this)
                        .load("https://media.istockphoto.com/photos/business-woman-lady-boss-in-beauty-salon-making-hairdress-and-looking-picture-id1147811403?k=20&m=1147811403&s=612x612&w=0&h=lBbmmhPxES33OgnJgkzvtURRSs_gRvD7kX65gETQ9r8=")
                        .apply(optionsb)
                        .into(bimageView19)

                val optionsf: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_no_pictures)
                        .error(R.drawable.ic_no_pictures)
                Glide.with(this)
                        .load("http://mdqualityapps.in/gallery/"+userProfileResponse.data?.get(0)?.gallery)
                        .apply(optionsf)
                        .into(fimageView19)

                    fragmentProfileBinding?.tvmess?.setOnClickListener {
//                        startActivity(
//                                LiveChatActivity.getCallingIntent(
//                                        requireActivity(),
//                                        userId.toString(),
//                                        userProfileResponse.data?.get(0)?.name!!
//                                )
//                        )
                    }

                    fragmentProfileBinding?.ftextView50?.setOnClickListener {
//                        startActivity(
////                                LiveChatActivity.getCallingIntent(
////                                        requireActivity(),
////                                        userId.toString(),
////                                        userProfileResponse.data?.get(0)?.name!!
////                                )
//                        )
                    }

                    fragmentProfileBinding?.textView20?.setOnClickListener {

                        if (fragmentProfileBinding?.textView129?.visibility == View.VISIBLE) {
                            fragmentProfileBinding?.textView129?.visibility = View.GONE
                            fragmentProfileBinding?.textView28?.visibility = View.GONE
                            fragmentProfileBinding?.chipGroup2?.visibility = View.GONE
                            fragmentProfileBinding?.textView25?.visibility = View.GONE
                            fragmentProfileBinding?.rdoGender?.visibility = View.GONE

                        } else {
                            fragmentProfileBinding?.textView129?.visibility = View.VISIBLE
                            fragmentProfileBinding?.textView28?.visibility = View.VISIBLE
                            fragmentProfileBinding?.chipGroup2?.visibility = View.VISIBLE
                            fragmentProfileBinding?.textView25?.visibility = View.VISIBLE
                            fragmentProfileBinding?.rdoGender?.visibility = View.VISIBLE
                        }

                    }

                    fragmentProfileBinding?.cvUser?.visibility = View.GONE
                    fragmentProfileBinding?.scBuss?.visibility = View.VISIBLE
                    fragmentProfileBinding?.scFree?.visibility = View.GONE

                    fragmentProfileBinding?.textView20?.setOnClickListener {

                        if (fragmentProfileBinding?.textView94?.visibility == View.VISIBLE) {
                            fragmentProfileBinding?.textView94?.visibility = View.GONE
                            fragmentProfileBinding?.textView28?.visibility = View.GONE
                            fragmentProfileBinding?.chipGroup2?.visibility = View.GONE
                            fragmentProfileBinding?.textView25?.visibility = View.GONE
                            fragmentProfileBinding?.rdoGender?.visibility = View.GONE

                            fragmentProfileBinding?.textView20?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_button, 0)

                        } else {
                            fragmentProfileBinding?.textView94?.visibility = View.VISIBLE
                            fragmentProfileBinding?.textView129?.visibility = View.VISIBLE
                            fragmentProfileBinding?.textView28?.visibility = View.VISIBLE
                            fragmentProfileBinding?.chipGroup2?.visibility = View.VISIBLE
                            fragmentProfileBinding?.textView25?.visibility = View.VISIBLE
                            fragmentProfileBinding?.rdoGender?.visibility = View.VISIBLE
                            fragmentProfileBinding?.textView20?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0)
                        }

                    }

                } else {

                fragmentProfileBinding?.ftextView20?.setOnClickListener {

                    if (fragmentProfileBinding?.ftextView94?.visibility == View.VISIBLE) {
                        fragmentProfileBinding?.ftextView94?.visibility = View.GONE
                        fragmentProfileBinding?.ftextView20?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_send_button,
                            0
                        )

                    } else {
                        fragmentProfileBinding?.ftextView94?.visibility = View.VISIBLE
                        fragmentProfileBinding?.ftextView20?.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.ic_up_arrow,
                            0
                        )

                    }

                }

                fragmentProfileBinding?.cvUser?.visibility = View.GONE
                fragmentProfileBinding?.scBuss?.visibility = View.GONE
                fragmentProfileBinding?.scFree?.visibility = View.VISIBLE

            }
        })
    }

    private fun updateReview(
            followStatus: String,
            rating: String,
            followerid: String,
            review: String
    ) {
        profileViewModel!!.updateReview(
                appPreference.USERID,
                followStatus,
                rating,
                followerid, review

        ).observe(requireActivity(), Observer { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileResponse

                if (userProfileResponse.message.equals("post details")) {
                    showToast(userProfileResponse.message)

                    dialog?.dismiss()

                } else {
                    showToast(userProfileResponse.message)
                }

            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun profileClick(status: Int) {
        statuss = status
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

    override fun onClick(status: Int) {
        if (status == 1) {
            startActivity(ReviewListActivity.getCallingIntent(requireContext()))
        } else {
            setDialogReview()
        }
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
                SignUpBusinessActivity.LEGACY_WRITE_PERMISSIONS,
                SignUpBusinessActivity.LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
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
                                    profileViewModel?.upload?.set(file)
                                    profileUpdate()
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
                                    profileViewModel?.upload?.set(file)
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
        }
    }

    private fun profileUpdate() {
        profileViewModel?.updateProfile()?.observe(this, Observer { response ->
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
            val mimeType = if (sourceFile.absolutePath
                            .contains("jpeg") || sourceFile.absolutePath.contains("jpg")
            ) "image/jpeg" else ""

            profileViewModel?.upload?.set(sourceFile)
            profileViewModel?.uploadmimeType?.set(mimeType)

            if (statuss == 1) {
                val options: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_no_pictures)
                        .error(R.drawable.ic_no_pictures)
                Glide.with(this)
                        .load(sourceFile)
                        .apply(options)
                        .into(bimageView19)
            } else if (statuss == 2) {
                val options: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_no_pictures)
                        .error(R.drawable.ic_no_pictures)
                Glide.with(this)
                        .load("https://media.istockphoto.com/photos/business-woman-lady-boss-in-beauty-salon-making-hairdress-and-looking-picture-id1147811403?k=20&m=1147811403&s=612x612&w=0&h=lBbmmhPxES33OgnJgkzvtURRSs_gRvD7kX65gETQ9r8=")
                        .apply(options)
                        .into(bimageView19)
            } else {
                val options: RequestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_no_pictures)
                        .error(R.drawable.ic_no_pictures)
                Glide.with(this)
                        .load(sourceFile)
                        .apply(options)
                        .into(fimageView19)
            }


        }
    }

    override fun onItemLickClicksOfProfile(id: String, gallery: String, user_id: String,position:Int) {
        TODO("Not yet implemented")
    }

}