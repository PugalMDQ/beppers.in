package com.mdq.social.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.response.Rating.Ratings
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.addlikecomments.AddLikeCommentsResponse
import com.mdq.social.app.data.response.recent.DataItem
import com.mdq.social.app.data.response.recent.RecentResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.response.updatedLikeUnlike.updatedLikeUnlike
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.home.HomeViewModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.app.helper.appsignature.AppSignatureHelper.Companion.TAG
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentHomeBinding
import com.mdq.social.ui.Firebase.Constants
import com.mdq.social.ui.models.User
import com.mdq.social.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import org.json.JSONObject


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeNavigator>(), HomeNavigator,
    HomeAdapter.ClickManager {

    private var homeViewModel: HomeViewModel? = null
    private var homeBinding: FragmentHomeBinding? = null
    private var homeAdapter: HomeAdapter? = null
    private var recentResponse: RecentResponse? = null
    private var preferenceManager: PreferenceManager? = null
    private var bottomSheetDialog: BottomSheetDialog? = null

    var fanduf: Int = 0
    var positions: Int? = null
    var profile: ImageView? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): BaseViewModel<HomeNavigator> {
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        return homeViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.homeViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding = getViewDataBinding()
        homeBinding?.homeViewModel = homeViewModel
        homeViewModel?.navigator = this
        homeAdapter =
            homeViewModel?.let { HomeAdapter(requireContext(), this, it, appPreference.USERID) }
        rv_home.adapter = homeAdapter
        rv_home.adapter = homeAdapter
        rv_home.setHasFixedSize(true)
        rv_home.setNestedScrollingEnabled(false)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {

                var firebaseToken = it.result.toString()
                Log.i("sanjai1243421321", firebaseToken)

            }
        }

        if (amIConnected()) {
            getProfile()
            if (!appPreference.ADMINBLOCK.equals("1")) {
                getRecent(appPreference.USERID)
            } else {
                homeBinding!!.BlockImage.visibility = View.VISIBLE
                homeBinding!!.blockText.visibility = View.VISIBLE
                rv_home.visibility = View.GONE
            }
            subscribeTOTopic("s", "s", "s")
        } else {
            showToast("No Internet")
            rv_home.visibility = View.VISIBLE
            rv_recent.visibility = View.GONE
            recentlistRecyclerViews(getPreferenceManager()?.getList()!!)
        }
    }

    private fun amIConnected(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getProfile() {
        homeViewModel!!.getUserProfileDetails(
            appPreference.USERID
        ).observe(requireActivity()) { response ->
            if (response?.data != null) {
                val userProfileResponse = response.data as UserProfileDetailResponse
                if (userProfileResponse != null) {
                    homeBinding!!.textView133.setText(userProfileResponse.data?.get(0)!!.name)
                    appPreference.NAME = userProfileResponse.data?.get(0)?.name.toString().trim()
                    appPreference.USERNAME =
                        userProfileResponse.data?.get(0)?.user_name.toString().trim()
                    appPreference.MOBILENU =
                        userProfileResponse.data?.get(0)?.mobile.toString().trim()
                    appPreference.FIREBASEUSERID =
                        userProfileResponse.data?.get(0)?.firebase_userid.toString()
                    if (userProfileResponse.data?.get(0)?.firebase_userid.toString().trim()
                            .isNullOrEmpty()
                    ) {
                        performRegitsration(
                            userProfileResponse.data?.get(0)?.email.toString().trim(),
                            userProfileResponse.data?.get(0)?.password.toString().trim()
                        )
                    }

                    if (userProfileResponse.data?.get(0)?.block.toString().trim().equals("true")) {
                        appPreference.ADMINBLOCK = "1"
                    } else {
                        appPreference.ADMINBLOCK = "0"
                    }

                    if (userProfileResponse.data?.get(0)?.profile_photo != null) {
                        try {
                            Glide.with(this).load(
                                "http://mdqualityapps.in/profile/" + userProfileResponse.data?.get(0)?.profile_photo
                            )
                                .into(imageView10)
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
    }

    private fun getRecent(userId: String) {
        homeViewModel!!.getRecent(userId)
            .observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val recentResponse = response.data as RecentResponse
                    if (recentResponse != null && recentResponse?.data != null) {
                        if (recentResponse.data.size == 0) {
                            if (!appPreference.ADMINBLOCK.equals("1")) {
                                rv_home.visibility = View.VISIBLE
                                rv_recent.visibility = View.GONE
                                recentlistRecyclerViews(getPreferenceManager()?.getList()!!)
                                rv_home?.scrollToPosition(positions!!)
                            } else {
                                rv_home.visibility = View.GONE
                            }
                        } else {
                            recentlistRecyclerViews(recentResponse.data)
                            getPreferenceManager()?.setList(recentResponse.data)
                            this.recentResponse = recentResponse
                            rv_home.visibility = View.VISIBLE
                            rv_recent.visibility = View.GONE
                            rv_home?.scrollToPosition(positions!!)
                        }
                    } else {
                        showToast(recentResponse.message)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    private fun recentlistRecyclerViews(dataItem: List<DataItem>) {

        if (dataItem != null && dataItem.size > 0) {
            homeBinding!!.executePendingBindings()
            homeAdapter!!.setHomeAdapter(dataItem)

            val urls: MutableList<String> = ArrayList()

            for (`object` in dataItem) {
                if (`object`.gallery != null && `object`.gallery.toString()
                        .endsWith(".mp4")
                ) urls.add(`object`.gallery.toString())
            }

            rv_home.smoothScrollBy(0, 1)
            rv_home.smoothScrollBy(0, -1)
        }
    }

    override fun onItemLickClick(
        position: Int,
        imageView32: ImageView,
        tvLikeCount: TextView,
        recentItem: DataItem,
        active: String,
        no_of_like: String
    ) {
        positions = position
        if (active.equals("0")) {
            homeViewModel!!.getAddLikeComments(
                recentItem.id.toString(),
                recentItem.user_id.toString(),
                active
            )
                .observe(requireActivity(), Observer { response ->
                    if (response?.data != null) {
                        val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                        if (addLikeCommentsResponse != null) {
                            if (addLikeCommentsResponse.message.equals("Like added successfully!")) {
                                imageView32.setImageResource(R.drawable.ic_heart_1fill)
                                getRecent(appPreference.USERID)
                                updtedLikeUnLike(tvLikeCount, recentItem.id.toString())
                                prepareNotificationMessage(
                                    recentItem.user_id.toString(),
                                    appPreference.USERID,
                                    appPreference.USERNAME
                                )
                                insertNotification(
                                    recentItem.id.toString(),
                                    recentItem.user_id.toString(), appPreference.USERNAME
                                )
                            }
                        }
                    }
                })
        } else if (active.equals("1")) {
            if (no_of_like != "0") {
                homeViewModel!!.getAddUnLikeComments(
                    recentItem.id.toString(),
                    recentItem.user_id.toString(),
                    active
                )
                    .observe(requireActivity(), Observer { response ->
                        if (response?.data != null) {
                            val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                            if (addLikeCommentsResponse != null) {
                                if (addLikeCommentsResponse.message.equals("Like removed successfully!")) {
                                    imageView32.setImageResource(R.drawable.ic_heart_1__1_)
                                    getRecent(appPreference.USERID)
                                    updtedLikeUnLike(tvLikeCount, recentItem.id.toString())
                                }
                            }
                        }
                    })
            }
        }
    }

    private fun insertNotification(postId: String, toID: String, name: String) {
        homeViewModel?.insertNotification(postId, toID, name)
            ?.observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val res = response.data as SignupResponse
                    if (res.message.equals("Notification added successfully")) {

                    } else {
                        showToast(res.message)
                    }
                }
            })
    }

    private fun updtedLikeUnLike(tvLikeCount: TextView, id: String) {
        homeViewModel?.UpdateLikeUnlike(id)?.observe(requireActivity(), Observer { response ->
            if (response?.data != null) {
                val updatedLikeList = response.data as updatedLikeUnlike
                if (updatedLikeList != null) {
                    tvLikeCount.setText(updatedLikeList.data?.get(0)?.no_of_likes.toString())
                }
            }
        })
    }

    override fun onItemSubscribeClick(position: Int, recentItem: DataItem, imageView: ImageView) {

    }

    override fun onItemProfileClick(position: Int, recentItem: DataItem) {

        startActivity(
            Intent(requireContext(), ProfileActivity::class.java).putExtra(
                "id",
                recentItem.user_id
            )
        )

    }

    override fun onShareClick(position: Int, get: DataItem) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "image/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, get.gallery)
        startActivity(sharingIntent)
    }

    override fun onMenuClick(position: Int, get: DataItem, star: TextView) {
        homeViewModel!!.getRating(get.user_id!!).observe(this, Observer { response ->

            val rating = response.data as Ratings
            if (rating.data != null) {
                star.setText("%.1f".format(rating.data.get(0).avg_rating!!.toDouble()))
            } else {
                star.setText("0")
            }
        })
    }

    override fun onHideClick(postid: String, position: Int, userId: String?) {
        if (userId.equals(appPreference.USERID)) {
            homeViewModel!!.deletePost(
                postid
            ).observe(this, Observer { response ->
                if (response?.data != null) {
                    val signupResponse = response.data as SignupResponse
                    if (signupResponse.message.equals("post has been deleted!")) {
                        getRecent(appPreference.USERID)
                        showToast("post has been deleted!")
                    } else {
                        showToast(signupResponse.message!!)
                    }
                }
            })
        } else {
            homeViewModel!!.getHidePost(postid).observe(this, Observer { response ->
                if (response?.data != null) {
                    val response = response.data as SignupResponse
                    if (response.message.equals("hide added successfully!")) {
                        showToast("Post hidden successfully!")
                        positions = position
                        getRecent(appPreference.USERID)
                    } else {
                        showToast(response.message)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
        }
    }

    override fun saveBookmark(postid: String, position: Int, userId: String?) {

        homeViewModel!!.SaveBookmark(postid).observe(this, Observer { response ->
            if (response?.data != null) {
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()

    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(requireContext())
        }
        return preferenceManager
    }

    private fun prepareNotificationMessage(to: String, from: String, name: String) {

        val NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC
        val NOTIFICATION_TITLE = name
        val NOTIFICATION_MESSAGE = "has liked your post!!"
        val NOTIFICATION_TYPE = "notificationType_LIKE"

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

    private fun performRegitsration(email: String, password: String) {
        if (email != null && password != null) {
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

        firebaseupdateUID(uid)

        val user = if (profileImageUrl == null) {
            User(uid, appPreference.USERNAME, null, appPreference.MOBILENU, null)
        } else {
            User(uid, appPreference.USERNAME, profileImageUrl, appPreference.MOBILENU, null)
        }

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(AppSignatureHelper.TAG, "Finally we saved the user to Firebase Database")
            }

            .addOnFailureListener {
                Log.d(AppSignatureHelper.TAG, "Failed to set value to database: ${it.message}")
            }

    }

    private fun firebaseupdateUID(uid: String) {
        homeViewModel!!.UpdateFireBase(appPreference.USERID!!, uid).observe(
            requireActivity()
        ) { response ->
            if (response?.data != null) {
                val signupResponse = response.data as SignupResponse

                if (signupResponse.message.equals("User updated successfully!")) {

                } else {
                    showToast(signupResponse!!.message.toString())
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        }
    }
}