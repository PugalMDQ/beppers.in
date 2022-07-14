package com.mdq.social.ui.business

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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
import com.mdq.social.PreferenceManager
import com.mdq.social.app.data.response.ShopItem
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.common.ResponseStatus
import com.mdq.social.app.data.response.profileupdate.ProfileUpdateResponse
import com.mdq.social.app.data.response.user_profile.DataItem
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.business.BusinessViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentBusinessBinding
import com.mdq.social.ui.businessupdate.BusinessUpdateActivity
import com.mdq.social.ui.freelanceupdate.FreelanceUpdateActivity
import com.mdq.social.ui.individual.IndividualActivity
import com.mdq.social.ui.profile.ProfileAdapter
import com.mdq.social.ui.signupbusiness.SignUpBusinessActivity
import kotlinx.android.synthetic.main.fragment_business.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.launch
import java.io.File
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mdq.social.app.data.response.RateCard.RateCardResponse
import com.mdq.social.app.data.response.Rating.Ratings
import com.mdq.social.app.data.response.ShopItemItem
import com.mdq.social.app.data.response.chatBlockStatus.ChatBlockedStatus
import com.mdq.social.app.data.response.followresponse.FollowResponse
import com.mdq.social.app.data.response.privacy.PrivacyDetail
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.helper.appsignature.AppSignatureHelper.Companion.TAG
import com.mdq.social.ui.Firebase.Constants
import com.mdq.social.ui.livechat.LiveChatActivity
import com.mdq.social.ui.models.User
import com.mdq.social.ui.notification.NotificationActivity
import com.mdq.social.ui.reviewlist.ReviewListActivity
import com.mdq.social.ui.signupbusiness.TimingItem
import com.mdq.social.ui.signupbusiness.TimingRequest
import com.mdq.social.utils.FileUtils
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BusinessProfileFragment : BaseFragment<FragmentBusinessBinding, BusinessProfileNavigator>(),
    BusinessProfileNavigator, EasyImage.EasyImageStateHandler, ProfileAdapter.ClickManager,
    MenuAdapter.clicks {

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, SignUpBusinessActivity::class.java)
        }

        const val IMAGE_PICK_CODE = 999
        const val LEGACY_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 456
        val LEGACY_WRITE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    }

    private var businessViewModel: BusinessViewModel? = null
    private var businessBinding: FragmentBusinessBinding? = null
    private var easyImage: EasyImage? = null
    private val easyImageState = Bundle()
    private var timingsAdapter: TimingsAdapter? = null
    private var profileAdapter: ProfileAdapter? = null
    lateinit var adapters: MenuAdapter
    lateinit var listItems: ArrayList<Uri>
    public var imageView17: ImageView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var preferenceManager: PreferenceManager? = null
    var mParam1: String? = null
    var name: String = ""
    var mobile: String = ""
    var emails: String = ""
    var userName: String = ""
    var image: String = ""
    var ToFireBaseID: String = ""
    var USERID: String = ""
    var TYPE: String = ""
    var FROMUSERID: String? = null
    var namesOfUser: String = ""
    var unf: Int = 0
    var increment: Int = 0
    var userProfileResponse1: UserProfileResponse? = null
    var mArrayUri: ArrayList<String>? = null
    var mmUri: String = ""
    var uri: ArrayList<Uri>? = null
    private var filess: ArrayList<File>? = null
    var file: File? = null
    var top: TextView? = null
    var dateTime: String? = null
    var UserIDFireBase: String? = null
    private var selectedPhotoUri: Uri? = null
    var privacydetails: Int? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_business
    }

    override fun getViewModel(): BaseViewModel<BusinessProfileNavigator> {
        businessViewModel =
            ViewModelProvider(this, viewModelFactory).get(BusinessViewModel::class.java)
        return businessViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.homeViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        businessBinding = getViewDataBinding()

        layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL,
            false
        )

        top = view.findViewById(R.id.top)

        imageView17 = view.findViewById(R.id.imageView17)

        listItems = ArrayList()
        easyImage = EasyImage.Builder(requireContext())
            .setChooserTitle("Pick media")
            .setCopyImagesToPublicGalleryFolder(true)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("Makeovers")
            .allowMultiple(true)
            .setStateHandler(this)
            .build()

        imageView17?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if (getArguments() != null) {
            mParam1 = getArguments()?.getString("message")
            businessBinding?.linMess?.visibility = View.VISIBLE
            businessBinding?.edit?.visibility = View.GONE
            businessBinding?.imageView17?.visibility = View.VISIBLE
            businessBinding?.imageView19?.isEnabled = false
            if (!mParam1.equals(appPreference.USERID)) {
                getFollowUnfollowcondition(mParam1)
                getprivacyDetail()
                getChatBlockStatus()
            }
        }
        if(mParam1.isNullOrEmpty()){
            businessBinding?.linMess?.visibility = View.INVISIBLE
        }
        if (mParam1?.trim().equals(appPreference.USERID.trim())) {
            businessBinding?.linMess?.visibility = View.INVISIBLE
            businessBinding?.edit?.visibility = View.VISIBLE
            businessBinding?.imageView19?.isEnabled = true
        }

        businessBinding!!.textView127.setOnClickListener {
            if (!mParam1.isNullOrEmpty()) {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        ReviewListActivity::class.java
                    ).putExtra("mparam", mParam1)
                )
            } else {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        ReviewListActivity::class.java
                    ).putExtra("mparam", appPreference.USERID)
                )
            }
        }

        adds.setOnClickListener {
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
            val todayDate: String = dateFormat.format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            dateTime = todayDate + "/" + currentTime + "/" + increment + "/"

            increment = increment + 1

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                232
            )

        }

        //for message
        businessBinding?.textView50?.setOnClickListener {
            if (ToFireBaseID != null) {
                var intent: Intent = Intent(requireContext(), LiveChatActivity::class.java)
                intent.putExtra("name", userName)
                intent.putExtra("image", image)
                intent.putExtra("to_id", mParam1)
                intent.putExtra("ToFireBaseID", ToFireBaseID)
                startActivity(intent)
            }
        }

        businessBinding?.imageView19?.setOnClickListener {
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

        businessBinding?.edit?.setOnClickListener {
            if (appPreference.USERGROUP.equals("business")) {
                startActivity(BusinessUpdateActivity.getCallingIntent(requireActivity()))
            } else if (appPreference.USERGROUP.equals("user")) {
                startActivity(IndividualActivity.getCallingIntent(requireActivity()))
            } else if (appPreference.USERGROUP.equals("freelance")) {
                startActivity(FreelanceUpdateActivity.getCallingIntent(requireActivity()))
            }
        }

        businessBinding?.follow?.setOnClickListener {
            if (unf == 0) {
                follow(mParam1!!)
            } else if (unf == 1) {
                unfollow(mParam1!!)
            }
        }

        var days: ArrayList<String> = ArrayList()
        days.add("Mon")
        days.add("Tues")
        days.add("Wed")
        days.add("Thus")
        days.add("Fri")
        days.add("sat")
        days.add("sun")
        if (appPreference.USERGROUP.equals("business")) {
            businessBinding?.adds?.visibility = View.VISIBLE
            businessBinding?.MenuListOfShop?.visibility = View.VISIBLE
            businessBinding!!.top.setText("BUSINESS")
            if (mParam1.equals(null)) {
                businessViewModel!!.getShopTiming(appPreference.USERID)
                    .observe(requireActivity(), Observer { response ->
                        var shopResponse = response.data as ShopItem
                        if (!shopResponse.data!!.isEmpty()) {
                            shoptiming(shopResponse.data!!)
                            timingsAdapter = TimingsAdapter(requireContext(), shopResponse, days)
                            businessBinding?.recyclerView2?.adapter = timingsAdapter
                        } else {
                            showToast(shopResponse.message)
                        }
                    })
            } else {
                businessViewModel!!.getShopTiming(mParam1!!)
                    .observe(requireActivity(), Observer { response ->

                        val shopResponse = response.data as ShopItem

                        if (!shopResponse.data!!.isEmpty()) {
                            shoptiming(shopResponse.data!!)
                            timingsAdapter = TimingsAdapter(requireContext(), shopResponse, days)
                            businessBinding?.recyclerView2?.adapter = timingsAdapter
                        } else {
                            showToast(shopResponse.message)
                        }
                    })
            }
        } else if (appPreference.USERGROUP.equals("user")) {
            businessBinding!!.top.setText("USER")
            businessBinding!!.textView90.visibility = View.GONE
            businessBinding!!.view6.visibility = View.GONE
            businessBinding!!.textView19.visibility = View.GONE
            businessBinding!!.textView127.visibility = View.GONE
            businessBinding!!.textView126.visibility = View.GONE
            businessBinding!!.textView20.visibility = View.GONE
            businessBinding!!.textView129.visibility = View.GONE
            businessBinding!!.email.visibility = View.GONE
            businessBinding!!.mobile.visibility = View.GONE
            businessBinding!!.textView28.visibility = View.GONE
            businessBinding!!.textView25.visibility = View.GONE
            businessBinding!!.rdoGender.visibility = View.GONE
            businessBinding!!.chipGroup2.visibility = View.GONE
            businessBinding!!.recyclerView.visibility = View.GONE
            businessBinding!!.recyclerView2.visibility = View.GONE
            businessBinding!!.Rate.visibility = View.GONE
            businessBinding!!.adds.visibility = View.GONE
            businessBinding!!.view4.visibility = View.GONE
            businessBinding!!.MenuListOfShop.visibility = View.GONE
            businessBinding!!.MenuListOfShop.visibility = View.GONE

        } else if (appPreference.USERGROUP.equals("freelance")) {
            businessBinding!!.top.setText("FREELANCER")
            businessBinding!!.textView90.visibility = View.GONE
            businessBinding!!.recyclerView2.visibility = View.GONE
            businessBinding!!.textView20.visibility = View.GONE
            businessBinding!!.textView129.visibility = View.GONE
            businessBinding!!.mobile.visibility = View.GONE
            businessBinding!!.email.visibility = View.GONE
            businessBinding!!.adds.visibility = View.VISIBLE
        }

        if (amIConnected()) {
            getProfile()
            getProfileDetail()
            getRateCards()
            if (!appPreference.FIREBASEUSERID.isNullOrEmpty()) {
                fetchUsers()
            }

        } else {
            showToast("No Internet")
        }

        if (mParam1 != null) {
            if (!appPreference.USERID.equals(mParam1)) {
                businessBinding?.adds?.visibility = View.GONE
            }
        }

        businessBinding?.textView20?.setOnClickListener {
            if (businessBinding?.textView129?.visibility == View.VISIBLE) {
                businessBinding?.textView129?.visibility = View.GONE
                businessBinding?.email?.visibility = View.GONE
                businessBinding?.mobile?.visibility = View.GONE
                businessBinding?.textView28?.visibility = View.GONE
                businessBinding?.chipGroup2?.visibility = View.GONE
                businessBinding?.textView25?.visibility = View.GONE
                businessBinding?.rdoGender?.visibility = View.GONE
                businessBinding?.textView90?.visibility = View.GONE
                businessBinding?.recyclerView2?.visibility = View.GONE
                businessBinding?.textView20?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_send_button,
                    0
                )
            } else {
                businessBinding?.textView129?.visibility = View.VISIBLE
                businessBinding?.email?.visibility = View.VISIBLE
                businessBinding?.mobile?.visibility = View.VISIBLE
                businessBinding?.textView28?.visibility = View.VISIBLE
                businessBinding?.chipGroup2?.visibility = View.VISIBLE
                businessBinding?.textView25?.visibility = View.VISIBLE
                businessBinding?.rdoGender?.visibility = View.VISIBLE
                businessBinding?.textView90?.visibility = View.VISIBLE
                businessBinding?.recyclerView2?.visibility = View.VISIBLE

                businessBinding?.textView20?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_up_arrow,
                    0
                )
            }
        }
        getRating()

        if (mParam1?.trim().equals(appPreference.USERID.trim())) {
            Log.i("entered","entered")
            businessBinding?.linMess?.visibility = View.INVISIBLE
            businessBinding?.edit?.visibility = View.VISIBLE
            businessBinding?.imageView19?.isEnabled = true
        }
    }


    private fun getprivacyDetail() {
        if (mParam1 != null) {
            businessViewModel!!.getPrivacy(
                mParam1!!
            ).observe(requireActivity(), Observer { response ->
                if (response.data != null) {
                    val privacydetail = response.data as PrivacyDetail
                    if (privacydetail.data != null) {
                        if (privacydetail.data!!.get(0).privacy!!.equals("1") && privacydetails == 0) {
                            businessBinding?.privateLayout?.visibility = View.VISIBLE
                            businessBinding?.textView40?.visibility = View.INVISIBLE
                            businessBinding?.gal?.visibility = View.INVISIBLE
                        }
                    }
                }
            })
        }
    }

    private fun getChatBlockStatus() {

        if (mParam1 != null) {
            businessViewModel!!.getChatBlockStatus(
                mParam1!!
            ).observe(requireActivity(), Observer { response ->
                if (response.data != null) {
                    val status = response.data as ChatBlockedStatus
                    if (status.data != null) {
                        if (status.data!!.get(0)?.chat_block!!.trim().equals(appPreference.USERID.trim())) {
                            businessBinding?.textView50?.isClickable = false
                            businessBinding?.textView50?.alpha=.5f
                        }
                    }
                }
            })
        }
    }

    private fun getRating() {
        if (mParam1 == null) {
            businessViewModel!!.getRating(
                appPreference.USERID
            ).observe(requireActivity(), Observer { response ->
                val Ratingss = response.data as Ratings
                if (Ratingss.message.equals("RateCard details")) {
                    if (!Ratingss.data?.get(0)?.avg_rating.isNullOrEmpty()) {
                        businessBinding?.textView126?.setText(
                            businessBinding?.textView126?.text.toString() + "%.1f".format(
                                Ratingss.data?.get(0)?.avg_rating!!.toDouble()
                            )
                        )
                    }
                } else {
                    showToast(Ratingss.message)
                }
            })
        } else {
            businessViewModel!!.getRating(
                mParam1.toString()
            ).observe(requireActivity(), Observer { response ->
                val Ratingss = response.data as Ratings
                if (Ratingss.message.equals("RateCard details")) {
                    if (!Ratingss.data?.get(0)?.avg_rating.isNullOrEmpty()) {
                        businessBinding?.textView126?.setText(
                            businessBinding?.textView126?.text.toString() + "%.1f".format(
                                Ratingss.data?.get(0)?.avg_rating!!.toDouble()
                            )
                        )
                    }
                } else {
                    showToast(Ratingss.message)
                }
            })
        }
    }

    private fun getFollowUnfollowcondition(mParam1: String?) {
        businessViewModel!!.getUserForFollowAndUnfollow(appPreference.USERID, mParam1!!)
            .observe(requireActivity(), Observer { response ->
                var followResponse = response.data as FollowResponse

                if (!followResponse.data.isNullOrEmpty()) {
                    if (followResponse.data!!.get(0).active.equals("0") && followResponse.data!!.get(
                            0
                        ).follow_request.equals("0")
                    ) {
                        unf = 0
                        businessBinding?.follow?.setText("Follow")
                        privacydetails = 0
                    } else if (followResponse.data!!.get(0).active.equals("0") && followResponse.data!!.get(
                            0
                        ).follow_request.equals("1")
                    ) {
                        unf = 2
                        businessBinding?.follow?.setText("Requested")
                        privacydetails = 0

                    } else if (followResponse.data!!.get(0).active.equals("1") &&
                        followResponse.data!!.get(0).follow_request.equals("0")
                    ) {
                        unf = 1
                        businessBinding?.follow?.setText("Unfollow")
                        privacydetails = 1
                    } else {
                        privacydetails = 0
                    }
                    if (followResponse.followback != null) {
                        if (followResponse.followback!!.get(0).followback!!.equals("1") &&
                            followResponse.data!!.get(0).active.equals("0") &&
                            followResponse.data!!.get(0).follow_request.equals("0")
                        ) {
                            unf = 0
                            businessBinding?.follow?.setText("Followback")
                            privacydetails = 0
                        }
                    }
                } else {
                    privacydetails = 0
                }
            })
    }

    @SuppressLint("ServiceCast")
    private fun amIConnected(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getRateCards() {
        if (mParam1 == null) {
            businessViewModel!!.getRateCard(
                appPreference.USERID
            ).observe(requireActivity(), Observer { response ->
                val RateCardResponses = response.data as RateCardResponse
                mArrayUri = ArrayList()
                if (RateCardResponses.data != null) {
                    businessBinding?.adds?.visibility = View.VISIBLE
                    businessBinding?.MenuListOfShop?.visibility = View.VISIBLE
                    businessBinding?.Rate?.visibility = View.VISIBLE
                    for (i in RateCardResponses.data.indices) {
                        var aa: String? = RateCardResponses.data.get(i).ratecard
                        if (aa != null) {
                            mArrayUri?.add(aa)
                        }
                    }
                    adapters =
                        MenuAdapter(requireContext(), mArrayUri!!, this, RateCardResponses.data!!)
                    businessBinding?.MenuListOfShop?.layoutManager = layoutManager
                    businessBinding?.MenuListOfShop?.adapter = adapters
                } else {
                    adapters =
                        MenuAdapter(requireContext(), mArrayUri!!, this, RateCardResponses.data!!)
                    businessBinding?.MenuListOfShop?.layoutManager = layoutManager
                    businessBinding?.MenuListOfShop?.adapter = adapters
                    MenuListOfShop.visibility = View.GONE
                }
            })

        } else {
            businessViewModel!!.getRateCard(
                mParam1!!
            ).observe(requireActivity(), Observer { response ->
                val RateCardResponses = response.data as RateCardResponse
                mArrayUri = ArrayList()
                if (RateCardResponses.data != null) {
                    for (i in RateCardResponses.data.indices) {
                        var aa: String? = RateCardResponses.data.get(i).ratecard
                        if (aa != null) {
                            mArrayUri?.add(aa)
                        }
                    }
                    adapters =
                        MenuAdapter(requireContext(), mArrayUri!!, this, RateCardResponses.data)
                    businessBinding?.MenuListOfShop?.layoutManager = layoutManager
                    businessBinding?.MenuListOfShop?.adapter = adapters
                } else {
                    MenuListOfShop.visibility = View.GONE
                }
            })
        }
    }

    private fun follow(

        followerid: String,
    ) {
        businessViewModel!!.updateReview(
            appPreference.USERID,
            followerid

        ).observe(requireActivity(), Observer { response ->

            val userProfileResponse = response.data as UserProfileResponse

            if (userProfileResponse.message.equals("Follow request sent successfully!")) {

                showToast(userProfileResponse.message)
                businessBinding?.follow?.setText("Unfollow")
                getFollowUnfollowcondition(mParam1)
                prepareNotificationMessage(
                    followerid,
                    appPreference.USERID,
                    appPreference.USERNAME,
                    "has sent a follow request to you!"
                )
                insertNotification(
                    "",
                    followerid,
                    appPreference.USERNAME,
                    "followRequest",
                    "has sent you a follow request"
                )

            } else if (userProfileResponse.message.equals("Follow accepted!")) {

                showToast(userProfileResponse.message)
                businessBinding?.follow?.setText("Unfollow")
                getFollowUnfollowcondition(mParam1)
                prepareNotificationMessage(
                    followerid,
                    appPreference.USERID,
                    appPreference.USERNAME,
                    "started following you!"
                )
                insertNotification(
                    "",
                    followerid,
                    appPreference.USERNAME,
                    "follow",
                    "has following you"
                )
                getProfileDetail()
            } else {
                showToast(userProfileResponse.message)
            }
        })
    }

    private fun unfollow(
        followerid: String,
    ) {
        businessViewModel!!.unfollow(
            appPreference.USERID,
            followerid
        ).observe(requireActivity(), Observer { response ->
            val userProfileResponse = response.data as UserProfileResponse
            if (userProfileResponse.message.equals("UnFollow request sent successfully!")) {
                showToast("Unfollowing the user!")
                businessBinding?.follow?.setText("follow")
                getFollowUnfollowcondition(mParam1)
                getProfileDetail()

            } else {
                showToast(userProfileResponse.message)
            }
        })
    }

    private fun getProfile() {
        if (mParam1 == null) {
            businessViewModel!!.getUserProfile(
                appPreference.USERID.toString()
            ).observe(requireActivity(), Observer { response ->
                if (response?.data != null) {

                    val userProfileResponse = response.data as UserProfileResponse
                    if (userProfileResponse.data != null) {
                        userProfileResponse1 = userProfileResponse
                        profileAdapter = context?.let {
                            ProfileAdapter(
                                it,
                                userProfileResponse.data as List<DataItem>, this
                            )
                        }

                        businessBinding?.gal?.adapter = profileAdapter

                        val optionsf: RequestOptions = RequestOptions()
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_logo)
                        Glide.with(this)
                            .load(
                                "http://mdqualityapps.in/gallery/" + userProfileResponse.data?.get(
                                    0
                                )?.gallery
                            )
                            .apply(optionsf)
                            .into(fimageView19)
                    } else {
                        businessBinding?.NOLAYOUT?.visibility = View.VISIBLE
                        businessBinding?.NOLAYOUT?.visibility = View.VISIBLE
                        businessBinding?.NOLAYOUT?.visibility = View.VISIBLE
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
        } else if (mParam1 != null) {
            businessViewModel!!.getUserProfile(
                mParam1.toString()
            ).observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val userProfileResponse = response.data as UserProfileResponse
                    if (userProfileResponse.data != null) {
                        profileAdapter = context?.let {
                            ProfileAdapter(
                                it,
                                userProfileResponse.data as List<DataItem>, this
                            )
                        }
                        businessBinding?.gal?.adapter = profileAdapter
                        val options: RequestOptions = RequestOptions()
                            .placeholder(R.drawable.ic_no_pictures)
                            .error(R.drawable.ic_no_pictures)
                        Glide.with(this)
                            .load(
                                "http://mdqualityapps.in/gallery/" + userProfileResponse.data?.get(
                                    0
                                )?.gallery
                            )
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
                            .load(
                                "http://mdqualityapps.in/gallery/" + userProfileResponse.data?.get(
                                    0
                                )?.gallery
                            )
                            .apply(optionsf)
                            .into(fimageView19)

                    } else {

                        businessBinding?.NOLAYOUT?.visibility = View.VISIBLE

                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
        }
    }

    private fun getProfileDetail() {

        if (mParam1 == null) {
            businessViewModel!!.getUserProfileDetails(
                appPreference.USERID.toString()
            ).observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val userProfileResponse = response.data as UserProfileDetailResponse
                    var uri: ArrayList<String>? = ArrayList()
                    if (userProfileResponse.data?.get(0)?.categories!!.contains(",")) {
                        val str = userProfileResponse.data?.get(0)?.categories!!
                        val arr: List<String> =
                            userProfileResponse.data?.get(0)?.categories!!.split(",")
                        val arr21 = ArrayList<String>()

                        for (i in arr.indices) {
                            if (arr.get(i).contains("Tattoo")) {
                                arr21.add("Tattoo & Piercing")
                            } else if (arr.get(i).contains("Spa")) {
                                arr21.add("Spa & Massage")
                            } else if (arr.get(i).contains("Waxing")) {
                                arr21.add("Waxing & Hair Removal")
                            } else if (arr.get(i).contains("Nail")) {
                                arr21.add("Nail Art & Care")
                            } else {
                                arr21.add(arr.get(i))
                            }
                        }

                        for (i in arr21.indices) {
                            if (!arr21.get(i).equals("")) {
                                uri?.add(arr21.get(i))
                                val chip = Chip(requireContext())
                                chip.text = arr21.get(i)
                                chip.setTextColor(Color.WHITE)
                                chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if ((i % 4) == 0) R.color.mate_red else if ((i % 3) == 0) R.color.mate_pink else R.color.txt_pink)
                                chip.chipCornerRadius = 10.0f
                                businessBinding?.chipGroup2?.addView(chip)
                            }
                        }
                    }

                    if (userProfileResponse.data?.get(0)?.type.equals("freelance")) {
                        if (userProfileResponse.data?.get(0)?.travel != null) {
                            if (userProfileResponse.data?.get(0)?.travel!!.contains("Travel other cities")) {
                                businessBinding!!.textView49.setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_baseline_airplanemode_active_24
                                    ),
                                    null
                                )
                            }
                        }
                    }

                    if (userProfileResponse.data?.get(0)?.service?.contains("Male")!!) {

                        businessBinding?.rdoMale?.visibility = View.VISIBLE
                    } else {
                        businessBinding?.rdoMale?.visibility = View.GONE

                    }

                    if (userProfileResponse.data?.get(0)?.service?.contains("female")!!) {

                        businessBinding?.rdoFemale?.visibility = View.VISIBLE

                    } else {
                        businessBinding?.rdoFemale?.visibility = View.GONE

                    }

                    if (userProfileResponse.data?.get(0)?.service?.contains("kids")!!) {

                        businessBinding?.rdoKids?.visibility = View.VISIBLE

                    } else {
                        businessBinding?.rdoKids?.visibility = View.GONE

                    }


                    name = userProfileResponse.data?.get(0)?.name.toString()
                    businessBinding?.textView27?.setText(userProfileResponse.data?.get(0)?.name)
                    businessBinding?.textView49?.setText(userProfileResponse.data?.get(0)?.user_name)
                    businessBinding?.textView53?.setText(userProfileResponse.data?.get(0)?.no_of_posts)
                    businessBinding!!.textView129.setText(userProfileResponse.data?.get(0)?.address)
                    businessBinding?.email?.setText(userProfileResponse.data?.get(0)?.email)
                    businessBinding?.mobile?.setText(userProfileResponse.data?.get(0)?.mobile)
                    businessBinding?.textView57?.setText(userProfileResponse.data?.get(0)?.no_of_following)
                    businessBinding?.textView55?.setText(userProfileResponse.data?.get(0)?.no_of_followers)
                    userName = userProfileResponse.data?.get(0)?.user_name.toString()
                    mobile = userProfileResponse.data?.get(0)?.mobile.toString()

                    if (userProfileResponse.data?.get(0)?.profile_photo != null) {
                        selectedPhotoUri = Uri.parse(
                            "https://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo
                        )
                        if (!appPreference.FIREBASEUSERID.isNullOrEmpty()) {
                            saveUserToFirebaseDatabase(
                                selectedPhotoUri.toString(),
                                userProfileResponse.data?.get(0)?.id.toString()
                            )
                        }
                    }

                    if (!userProfileResponse.data?.get(0)?.description?.isNullOrEmpty()!!) {
                        businessBinding?.textView52?.setText(userProfileResponse.data?.get(0)?.description)
                    }

                    if (userProfileResponse.data?.get(0)?.no_of_followers!!.contains("-")) {
                        businessBinding?.textView55?.setText("0")
                    } else {
                        businessBinding?.textView55?.setText(userProfileResponse.data?.get(0)?.no_of_followers)
                    }

                    if (userProfileResponse.data?.get(0)?.no_of_following!!.contains("-")) {

                        businessBinding?.textView57?.setText("0")

                    } else {
                        businessBinding?.textView57?.setText(userProfileResponse.data?.get(0)?.no_of_following)
                    }

                    if (!userProfileResponse.data?.get(0)?.profile_photo!!.isEmpty()) {
                        Glide.with(this).load(
                            "http://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo
                        )
                            .into(businessBinding?.imageView19!!)
                    }
                }
            })

        } else if (mParam1 != null) {
            businessViewModel!!.getUserProfileDetails(
                mParam1.toString()
            ).observe(requireActivity(), Observer { response ->
                if (response?.data != null) {

                    val userProfileResponse = response.data as UserProfileDetailResponse
                    businessBinding?.textView27?.setText(userProfileResponse.data?.get(0)?.name)
                    businessBinding?.textView49?.setText(userProfileResponse.data?.get(0)?.user_name)
                    businessBinding?.textView53?.setText(userProfileResponse.data?.get(0)?.no_of_posts)
                    businessBinding?.textView55?.setText(userProfileResponse.data?.get(0)?.no_of_followers)
                    businessBinding?.textView57?.setText(userProfileResponse.data?.get(0)?.no_of_following)
                    businessBinding!!.top.setText(userProfileResponse.data?.get(0)?.type)
                    businessBinding!!.textView129.setText(userProfileResponse.data?.get(0)?.address)
                    businessBinding?.email?.setText(userProfileResponse.data?.get(0)?.email)
                    businessBinding?.mobile?.setText(userProfileResponse.data?.get(0)?.mobile)
                    businessBinding?.travel?.setText(userProfileResponse.data?.get(0)?.travel)

                    ToFireBaseID = userProfileResponse.data?.get(0)?.firebase_userid.toString()

                    userName = userProfileResponse.data?.get(0)?.user_name.toString()
                    USERID = userProfileResponse.data?.get(0)?.id.toString()
                    emails = userProfileResponse.data?.get(0)?.email.toString()
                    TYPE = userProfileResponse.data?.get(0)?.type.toString()

                    if (appPreference.TYPE.equals(TYPE)) {
                        businessBinding?.textView50?.visibility = View.GONE
                    }

                    if (appPreference.TYPE.equals("freelance") && TYPE.equals("business")) {
                        businessBinding?.textView50?.visibility = View.GONE
                    }

                    if (appPreference.TYPE.equals("business") && TYPE.equals("freelance")) {
                        businessBinding?.textView50?.visibility = View.GONE
                    }

                    if (userProfileResponse.data?.get(0)?.type.equals("user")) {
                        businessBinding!!.top.setText("USER")
                        businessBinding!!.textView90.visibility = View.GONE
                        businessBinding!!.view6.visibility = View.GONE
                        businessBinding!!.textView19.visibility = View.GONE
                        businessBinding!!.textView127.visibility = View.GONE
                        businessBinding!!.textView126.visibility = View.GONE
                        businessBinding!!.textView20.visibility = View.GONE
                        businessBinding!!.textView129.visibility = View.GONE
                        businessBinding!!.email.visibility = View.GONE
                        businessBinding!!.mobile.visibility = View.GONE
                        businessBinding!!.textView28.visibility = View.GONE
                        businessBinding!!.textView25.visibility = View.GONE
                        businessBinding!!.rdoGender.visibility = View.GONE
                        businessBinding!!.chipGroup2.visibility = View.GONE
                        businessBinding!!.recyclerView.visibility = View.GONE
                        businessBinding!!.recyclerView2.visibility = View.GONE
                        businessBinding!!.Rate.visibility = View.GONE
                        businessBinding!!.adds.visibility = View.GONE
                        businessBinding!!.view4.visibility = View.GONE
                        businessBinding!!.MenuListOfShop.visibility = View.GONE
                        businessBinding!!.MenuListOfShop.visibility = View.GONE

                    } else if (userProfileResponse.data?.get(0)?.type.equals("freelance")) {

                        businessBinding!!.top.setText("FREELANCER")
                        businessBinding!!.recyclerView2.visibility = View.GONE
                        businessBinding!!.MenuListOfShop.visibility = View.VISIBLE
                        businessBinding!!.adds.visibility = View.GONE
                        businessBinding!!.textView20.visibility = View.GONE
                        businessBinding!!.textView129.visibility = View.GONE
                        businessBinding!!.email.visibility = View.GONE
                        businessBinding!!.mobile.visibility = View.GONE
                        businessBinding!!.textView90.visibility = View.GONE
                        businessBinding!!.recyclerView2.visibility = View.GONE
                        businessBinding!!.textView19.visibility = View.VISIBLE
                        businessBinding!!.textView126.visibility = View.VISIBLE
                        businessBinding!!.textView127.visibility = View.VISIBLE
                        businessBinding!!.recyclerView.visibility = View.VISIBLE
                        businessBinding!!.view4.visibility = View.VISIBLE
                        businessBinding!!.Rate.visibility = View.VISIBLE
                        businessBinding!!.textView28.visibility = View.VISIBLE
                        businessBinding!!.chipGroup2.visibility = View.VISIBLE
                        businessBinding!!.textView25.visibility = View.VISIBLE
                        businessBinding!!.rdoGender.visibility = View.VISIBLE
                        businessBinding!!.travel.visibility = View.VISIBLE

                    } else if (userProfileResponse.data?.get(0)?.type.equals("business")) {
                        top?.setText("BUSINESS")
                        businessBinding!!.textView90.visibility = View.VISIBLE
                        businessBinding!!.view6.visibility = View.VISIBLE
                        businessBinding!!.textView19.visibility = View.VISIBLE
                        businessBinding!!.textView127.visibility = View.VISIBLE
                        businessBinding!!.textView126.visibility = View.VISIBLE
                        businessBinding!!.textView20.visibility = View.VISIBLE
                        businessBinding!!.textView129.visibility = View.VISIBLE
                        businessBinding!!.mobile.visibility = View.VISIBLE
                        businessBinding!!.email.visibility = View.VISIBLE
                        businessBinding!!.textView28.visibility = View.VISIBLE
                        businessBinding!!.textView25.visibility = View.VISIBLE
                        businessBinding!!.rdoGender.visibility = View.VISIBLE
                        businessBinding!!.chipGroup2.visibility = View.VISIBLE
                        businessBinding!!.recyclerView.visibility = View.VISIBLE
                        businessBinding!!.recyclerView2.visibility = View.VISIBLE
                        businessBinding!!.Rate.visibility = View.VISIBLE
                        businessBinding!!.adds.visibility = View.GONE
                        businessBinding!!.view4.visibility = View.VISIBLE
                        businessBinding!!.MenuListOfShop.visibility = View.VISIBLE

                        var days: ArrayList<String> = ArrayList()
                        days.add("Mon")
                        days.add("Tues")
                        days.add("Wed")
                        days.add("Thus")
                        days.add("Fri")
                        days.add("sat")
                        days.add("sun")
                        businessViewModel!!.getShopTiming(mParam1!!)
                            .observe(requireActivity(), Observer { response ->

                                val shopResponse = response.data as ShopItem

                                if (!shopResponse.data!!.isEmpty()) {

                                    timingsAdapter =
                                        TimingsAdapter(requireContext(), shopResponse, days)
                                    businessBinding?.recyclerView2?.adapter = timingsAdapter
                                    shoptiming(shopResponse.data!!)
                                } else {
                                    showToast(shopResponse.message)
                                }
                            })
                    }

                    if (!userProfileResponse.data?.get(0)?.description?.isNullOrEmpty()!!) {
                        businessBinding?.textView52?.setText(userProfileResponse.data?.get(0)?.description)
                    }

                    if (userProfileResponse.data?.get(0)?.service?.contains("Male")!!) {

                        businessBinding?.rdoMale?.visibility = View.VISIBLE
                    } else {
                        businessBinding?.rdoMale?.visibility = View.GONE

                    }

                    if (userProfileResponse.data?.get(0)?.service?.contains("female")!!) {
                        businessBinding?.rdoFemale?.visibility = View.VISIBLE

                    } else {
                        businessBinding?.rdoFemale?.visibility = View.GONE

                    }

                    if (userProfileResponse.data?.get(0)?.service?.contains("kids")!!) {
                        businessBinding?.rdoKids?.visibility = View.VISIBLE

                    } else {
                        businessBinding?.rdoKids?.visibility = View.GONE

                    }

                    if (userProfileResponse.data?.get(0)?.no_of_followers!!.contains("-")) {
                        businessBinding?.textView55?.setText("0")
                    } else {
                        businessBinding?.textView55?.setText(userProfileResponse.data?.get(0)?.no_of_followers)
                    }

                    if (userProfileResponse.data?.get(0)?.no_of_following!!.contains("-")) {

                        businessBinding?.textView57?.setText("0")
                    } else {
                        businessBinding?.textView57?.setText(userProfileResponse.data?.get(0)?.no_of_following)
                    }

                    var uri: ArrayList<String>? = ArrayList()
                    if (userProfileResponse.data?.get(0)?.categories!!.contains(",")) {
                        val str = userProfileResponse.data?.get(0)?.categories!!
                        val arr: List<String> =
                            userProfileResponse.data?.get(0)?.categories!!.split(",")
                        val arr21 = ArrayList<String>()

                        for (i in arr.indices) {
                            if (arr.get(i).contains("Tattoo")) {
                                arr21.add("Tattoo & Piercing")
                            } else if (arr.get(i).contains("Spa")) {
                                arr21.add("Spa & Massage")
                            } else if (arr.get(i).contains("Waxing")) {
                                arr21.add("Waxing & Hair Removal")
                            } else if (arr.get(i).contains("Nail")) {
                                arr21.add("Nail Art & Care")
                            } else {
                                arr21.add(arr.get(i))
                            }
                        }

                        for (i in arr21.indices) {
                            if (!arr21.get(i).equals("")) {
                                uri?.add(arr21.get(i))
                                val chip = Chip(requireContext())
                                chip.text = arr21.get(i)
                                chip.setTextColor(Color.WHITE)
                                chip.setChipBackgroundColorResource(if ((i % 5) == 0) R.color.txt_orange else if ((i % 4) == 0) R.color.mate_red else if ((i % 3) == 0) R.color.mate_pink else R.color.txt_pink)
                                chip.chipCornerRadius = 10.0f
                                businessBinding?.chipGroup2?.addView(chip)
                            }
                        }
                    }

                    if (!userProfileResponse.data?.get(0)?.profile_photo!!.isEmpty()) {
                        Glide.with(this).load(
                            "http://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo
                        )
                            .into(businessBinding?.imageView19!!)
                        image =
                            "http://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo
                    }
                }
            })
        }
    }

    private fun shoptiming(data: List<ShopItemItem?>) {

        var timingRequest = TimingRequest()

        var timingItemList = ArrayList<TimingItem>()

        for (i in data.indices) {
            var timingItem = TimingItem()
            timingItem.day = data.get(i)?.day
            timingItem.morningTime = data.get(i)?.from_time
            timingItem.eveningTime = data.get(i)?.to_time
        }

        val gsonBuilder1 = GsonBuilder()

        val gson1: Gson = gsonBuilder1.create()

        timingRequest.shopTime = timingItemList

        val JSONObject: String = gson1.toJson(timingItemList)

//        businessViewModel?.shopTime?.set(JSONObject)

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String?, user_id: String) {
        val uid = appPreference.FIREBASEUSERID
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        if (!appPreference.FIREBASEUSERID.isNullOrEmpty()) {
            val user = if (profileImageUrl == null) {
                User(uid, userName, null, mobile, user_id)
            } else {
                User(uid, userName, profileImageUrl, mobile, user_id)
            }

            try {
                ref.setValue(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "Finally we saved the user to Firebase Database")
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Failed to set value to database: ${it.message}")
                    }
            } catch (e: Exception) {

            }
        }
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
                                businessViewModel?.upload?.set(filess)
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
                                businessViewModel?.upload?.set(filess)
                                profileUpdate()

                            }
                        }

                        override fun onImagePickerError(error: Throwable, source: MediaSource) {
                            error.printStackTrace()
                        }

                        override fun onCanceled(source: MediaSource) {

                        }
                    })
            } else if (requestCode == 232) {
                mArrayUri = ArrayList()

                if (data?.getClipData() != null) {
                    val cout: Int? = data?.getClipData()?.getItemCount()
                    if (cout != null) {
                        val imageurl: Uri? = data?.getClipData()?.getItemAt(0)?.getUri()
                        if (imageurl != null) {
                            mmUri = imageurl?.toString()
                            var uuri = Uri.parse(mmUri)
                            val path =
                                FileUtils.getPath(context, imageurl)
                            if (path != null) {
                                var file1 = File(path)
                                filess?.add(file1)
                                file = file1
                                businessViewModel?.upload?.set(File(path))
                                mArrayUri?.clear()
                                filess?.clear()
                                uploadratecards()
                            } else {
                                showToast("please select image")
                            }
                        } else {
                            showToast("please select image")
                        }
                    }
                } else {
                    val imageurl: Uri? = data?.getData()
                    mArrayUri?.add(imageurl!!.toString())

                    if (imageurl != null) {
                        mmUri = imageurl?.toString()
                        var uuri = Uri.parse(mmUri)
                        val path =
                            FileUtils.getPath(context, imageurl)
                        if (path != null) {
                            var file1 = File(path)
                            filess?.add(file1)
                            file = file1
                            businessViewModel?.upload?.set(File(path))
                            mArrayUri?.clear()
                            filess?.clear()
                            uploadratecards()
                        }
                    }
                }
            }
        }
    }

    private fun uploadratecards() {
        businessViewModel?.PostRateCard(appPreference.USERID, dateTime!!)
            ?.observe(this, Observer { response ->
                if (response?.data != null) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        getRateCards()
                    }, 1000)
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    private fun profileUpdate() {
        businessViewModel?.updateProfile(name!!)?.observe(this, Observer { response ->
            if (response?.data != null) {
                var profileUpdateResponse = response.data as ProfileUpdateResponse
                if (profileUpdateResponse != null && profileUpdateResponse.status == ResponseStatus.SUCCESS) {

                    showToast("Profile pic updated")

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

            businessViewModel?.upload?.set(sourceFile)
            businessViewModel?.uploadmimeType?.set(mimeType)

            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.ic_no_pictures)
                .error(R.drawable.ic_no_pictures)
            Glide.with(this)
                .load(sourceFile)
                .apply(options)
                .into(imageView19)
        }
    }

    override fun onItemLickClicksOfProfile(
        id: String,
        gallery: String,
        user_id: String,
        position: Int
    ) {
        startActivity(
            Intent(requireContext(), NotificationActivity::class.java)
                .putExtra("ProfileAdapter", "ProfileAdapter")
                .putExtra("position", position)
                .putExtra("user_id", user_id)
        )
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataSnapshot.children.forEach {
                    @Suppress("NestedLambdaShadowedImplicitParameter")
                    it.getValue(User::class.java)?.let {
                        if (it.userID != null) {
                            if (it.userID == USERID) {
                                UserIDFireBase = it.uid
                                Log.i("UserIDFireBase", UserIDFireBase!!)
                            }
                            Log.i("FROMUSERIDFIRE", it.userID)
                            Log.i("FROMUSERIDAPP", appPreference.USERID)
                            if (it.userID == appPreference.USERID) {
                                FROMUSERID = it.uid
                                Log.i("FROMUSERID", FROMUSERID!!)
                            }
                        }
                    }
                }
            }
        })
    }

    override fun click(id: String, url: String) {

        var dialog = Dialog(requireActivity(), R.style.dialog_center)
        dialog.setContentView(R.layout.dialog_for_ratecard)
        var urlImage = dialog.findViewById<ImageView>(R.id.RateCardURl)
        var delete = dialog.findViewById<ImageView>(R.id.deleteImage)

        if (mParam1 != null) {
            if (!appPreference.USERID.equals(mParam1)) {
                delete.visibility = View.GONE
            }
        }

        Glide.with(requireContext()).load(url)
            .into(urlImage)
        dialog.show()
        delete.setOnClickListener {
            businessViewModel?.DeleteRateCard(id!!)?.observe(this, Observer { response ->
                if (response?.data != null) {
                    var profileUpdateResponse = response.data as SignupResponse
                    if (profileUpdateResponse.message.equals("card deleted successfully!")) {
                        dialog.dismiss()
                        getRateCards()
                    } else {
                        showToast(profileUpdateResponse.message!!)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
        }
    }

    private fun prepareNotificationMessage(
        to: String,
        from: String,
        name: String,
        message: String
    ) {

        val NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC
        val NOTIFICATION_TITLE = name
        val NOTIFICATION_MESSAGE = message
        val NOTIFICATION_TYPE = "notificationType_Follow"

        //prepare json
        val notificationJo = JSONObject()
        val notificationBodyJo = JSONObject()

        try {
            //what to send
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE)
            notificationBodyJo.put("to_id", to)
            notificationBodyJo.put("from_id", from)
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE)
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE)

            //where to send
            notificationJo.put("to", NOTIFICATION_TOPIC) //to all who subscribed
            notificationJo.put("data", notificationBodyJo)

        } catch (e: Exception) {

            showToast(e.message)
        }
        sendFcmNotification(notificationJo)
        subscribeTOTopic(to, from, NOTIFICATION_TOPIC)

    }

    private fun subscribeTOTopic(buyerId: String, sellerId: String, notificationTopic: String) {

        Firebase.messaging.subscribeToTopic(Constants.FCM_TOPIC)
            .addOnCompleteListener { task ->
                var msg = "R.string.msg_subscribed"
                if (!task.isSuccessful) {
                    msg = " R.string.msg_subscribe_failed"
                }
                Log.d(TAG, msg)
            }
    }

    private fun sendFcmNotification(notificationJo: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo,
                Response.Listener { response: JSONObject? -> },
                Response.ErrorListener {
                    Toast.makeText(requireContext(), "" + it.message, Toast.LENGTH_SHORT).show()
                }
            ) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    //put request headers
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "key=" + Constants.FCM_KEY
                    return headers
                }
            }
        //enque the volley request
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)

    }

    private fun insertNotification(
        postId: String,
        toID: String,
        name: String,
        type: String,
        message: String
    ) {
        businessViewModel?.insertNotification(postId, toID, name, type, message)
            ?.observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val res = response.data as SignupResponse
                    if (res.message.equals("Notification added successfully")) {
                    } else {
                        res.message?.let { showToast(it) }
                    }
                }
            })
    }

}
