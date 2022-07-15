package com.mdq.social.ui.addpost

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.app.data.response.addalbumresponse.AddAlbumResponse
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.followresponse.FollowResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItems
import com.mdq.social.app.data.response.getshopAlbumDetails.UserSearchDetailResponse
import com.mdq.social.app.data.viewmodels.addpost.AddPostViewModel
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityAddPostBinding
import com.mdq.social.ui.Firebase.Constants
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.signupfreelancer.CategoryAdapter
import com.mdq.social.utils.FileUtils
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.activity_block.*
import kotlinx.android.synthetic.main.activity_signup_business.*
import kotlinx.android.synthetic.main.activity_signup_freelancer.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class   AddpostActivity : BaseActivity<ActivityAddPostBinding, AddPostNavigator>(), AddPostNavigator,
    CategoryAdapter.ClickManager, SliderAdapter.ClickManager, View.OnClickListener, TextWatcher,profileSearchAdapterForAddpost.ClickManager {

    @Inject
    override lateinit var appPreference: AppPreference
    private var imgPath: String? = null
    private var videoPath: String? = null
    private var addPostViewModel: AddPostViewModel? = null
    private var profileSearchAdapter: profileSearchAdapterForAddpost? = null
    private var activityAddPostBinding: ActivityAddPostBinding? = null
    private var dialog1: Dialog? = null
    private var count:String?=null
    private var dataItem: DataItem? = null
    private var file:ArrayList<File>?=null
    private var filess:ArrayList<File>?=null
    private var bitmap:String?=null
    private var ii:String?=null
    var getShopAlbumDetailsResponse:ArrayList<DataItems>?=null
    private var filesr:File?=null
    val userIDs = ArrayList<String>()
    var chipSearch1:Chip ?= null
    private var filer:File?=null
    private var preferenceManager:PreferenceManager?=null
    private var categoryList = ArrayList<DataItem>()
    var category = ""
    var mArrayUri: ArrayList<String>? = null
    var uri: ArrayList<Uri>?=null
    var sliderAdapter:SliderAdapter?=null
    var uuri: String?=null
    var jj=0
    var search=0
    var tagID:String?="null"
    override fun getLayoutId(): Int {
        return R.layout.activity_add_post
    }

    override fun getViewBindingVarible(): Int {
        return BR.profileViewModel
    }

    override fun getViewModel(): BaseViewModel<AddPostNavigator> {
        addPostViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddPostViewModel::class.java)
        return addPostViewModel!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddPostBinding = getViewDataBinding()
        chipSearch1=Chip(this)
        followerList()

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        if (intent.extras != null) {
            imgPath = intent?.extras?.getString(AppConstants.IMAGE)
            videoPath = intent?.extras?.getString(AppConstants.VIDEOS)
            bitmap = intent?.extras?.getString("Bitmap")
            mArrayUri = ArrayList<String>()
            mArrayUri = intent?.getStringArrayListExtra("AAA") as ArrayList<String>?
            uuri = intent?.extras?.getString("uri")

            if (imgPath != null) {
                count = "1"
                Glide.with(this).load(Uri.parse("file://" + imgPath))
                    .into(activityAddPostBinding?.img!!)
                activityAddPostBinding?.img!!.visibility = View.VISIBLE
                activityAddPostBinding?.videoView!!.visibility = View.GONE
                filess?.add(File(imgPath))
            }
                if (bitmap != null) {
                    uri = ArrayList<Uri>()
                    uri!!.add(Uri.parse(bitmap.toString()))
                    filess = ArrayList()
                        var uri: Uri = Uri.parse(bitmap.toString())
                        val path =
                            FileUtils.getPath(this, uri)
                        var file = File(path)
                        filess?.add(file)
                    addPostViewModel?.returnUri?.clear()
                    addPostViewModel?.returnUri?.add(uri!!)
                    addPostViewModel?.path?.addAll(filess!!)
                }
            else if (videoPath != null) {
                    count="2"
                    activityAddPostBinding?.img!!.visibility = View.GONE
                    activityAddPostBinding?.imageSlider!!.visibility = View.GONE
                    activityAddPostBinding?.videoView!!.visibility = View.VISIBLE
                    activityAddPostBinding?.videoView?.setVideoURI(Uri.parse(videoPath));
                    val mediaController = MediaController(this)
                    mediaController.setAnchorView(activityAddPostBinding?.videoView!!)
                    activityAddPostBinding?.videoView!!.setMediaController(mediaController)
                    activityAddPostBinding?.videoView?.start()
                    filesr=File(videoPath)
                    addPostViewModel?.path?.add(filesr!!)
                    val filePath: String =
                        FileUtils.getPath(this, Uri.parse(uuri))
                    filer = File(filePath)
                    addPostViewModel?.thumb?.set(filer)
            }
            else if(mArrayUri!=null){
                updates()
            }
        }

        if(appPreference.USERGROUP.
            equals("user")) {
            activityAddPostBinding?.edtLocation?.visibility=View.VISIBLE
            activityAddPostBinding?.textView21?.visibility=View.VISIBLE
        }

        activityAddPostBinding?.edtTag?.setOnClickListener {
            addPostViewModel!!.getCategory()
                .observe(this, Observer { response ->
                    if (response?.data != null) {
                        val categoryResponse = response.data as CategoryResponse
                        setDialogCategory(categoryResponse)
                    } else {
                    }
                })
        }

        activityAddPostBinding?.edtLocation?.addTextChangedListener(this)

        activityAddPostBinding?.tvConfirm?.setOnClickListener {


//            for (i in 0 until chipGroupAdd.childCount) {
//                val chip = chipGroupAdd.getChildAt(i) as Chip
//                category =
//                    category + chip.text
//                        .toString() + (if (i != chipGroup2UP.childCount - 1) "," else "")
//            }

            val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
            val todayDate: String = dateFormat.format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            val dateTime=todayDate+"/"+currentTime+"/"

            if (dataItem == null) {
                showToast(getString(R.string.please_select_category))
                return@setOnClickListener
            }

            activityAddPostBinding?.tvConfirm?.isClickable=false

            addPostViewModel!!.updatePost(
                appPreference.USERID,
                dateTime,
                category,
                activityAddPostBinding?.edtDescription?.text.toString(),
                tagID!!
            ).observe(this, Observer { response ->
                    if (response?.data != null) {
                        val addAlbumResponse = response.data as AddAlbumResponse

                        if (addAlbumResponse.message.equals("Post added successfully!")) {

                            if(userIDs!=null) {
                                prepareNotificationMessage(
                                    userIDs!!,
                                    appPreference.USERID,
                                    appPreference.USERNAME
                                )
                            }

                            showToast(addAlbumResponse.message!!)
                            startActivity(Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        } else {
                            showToast(addAlbumResponse.message!!)
                            activityAddPostBinding?.tvConfirm?.isClickable=true
                        }
                    } else
                    {

                    }
                })
        }

        activityAddPostBinding?.imageView?.setOnClickListener {
            finish()
        }

    }

    private fun followerList() {

        addPostViewModel!!.getfollowFollowingList(
            appPreference.USERID,
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val followResponse = response.data as FollowResponse
                if (followResponse.data!=null) {
                    for (i in followResponse.data!!.indices) {
                        if (followResponse.data?.get(i)?.active != null) {
                            if (followResponse.data.get(i).active!!.equals("1")){
                                if(!appPreference.USERID.equals(followResponse.data.get(i).id.toString()!!)) {
                                    userIDs.add(followResponse.data.get(i).id.toString()!!)
                                }
                            }
                        }
                    }
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun search() {
        addPostViewModel!!.getSearchUserDetails(
            activityAddPostBinding?.edtLocation?.text.toString()
        )
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val getShopAlbumDetailsResponse =
                        response.data as UserSearchDetailResponse
                    if (getShopAlbumDetailsResponse != null && getShopAlbumDetailsResponse?.data != null) {

                        this.getShopAlbumDetailsResponse= ArrayList<DataItems>()

                        for(i in getShopAlbumDetailsResponse?.data.indices){
                            if ( getShopAlbumDetailsResponse?.data?.get(i)?.type.equals("freelancer") ||  getShopAlbumDetailsResponse?.data?.get(i)?.type.equals(
                                    "business"
                                )
                            ) {
                                this.getShopAlbumDetailsResponse!!.add(getShopAlbumDetailsResponse.data?.get(i))
                            }
                            }

                        profileSearchAdapter = profileSearchAdapterForAddpost(
                            this.getShopAlbumDetailsResponse,
                            this,this
                        )
                        activityAddPostBinding?.rvSearch?.visibility=View.VISIBLE
                        activityAddPostBinding?.rvSearch?.adapter=profileSearchAdapter
                    } else {

                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    private fun setDialogCategory(categoryResponse: CategoryResponse) {

        var rvCategory = findViewById<RecyclerView>(R.id.rv_cat)
        if(jj==0) {
            jj=1
            rvCategory?.layoutManager = LinearLayoutManager(this)

            var categoryAdapter = CategoryAdapter(
                this,
                categoryResponse?.data as List<DataItem>,
                this
            )
            rvCategory?.adapter = categoryAdapter
            rvCategory?.visibility = View.VISIBLE
            categoryList = categoryResponse.data as ArrayList<DataItem>
        }else if(jj==1){
            rvCategory?.visibility = android.view.View.GONE
            jj=0
        }
    }

    override fun onItemClick(dataItem: DataItem, position: Int) {
        this.dataItem = dataItem

        if(!chipGroupAdd.childCount.equals(null)) {
            for (i in 0 until chipGroupAdd.childCount) {
                val chip = chipGroupAdd.getChildAt(i) as Chip
                if (chip.text.equals(dataItem.name)) {
                    showToast("Already added")
                    return
                }
            }
        }

        val chip = Chip(this)
        chip.text = dataItem.name.toString()
        chip.setTag(dataItem.id)
        chip.setChipBackgroundColorResource(if ((position % 2) == 0) R.color.txt_orange else R.color.txt_pink)
        chip.isCloseIconVisible = true
        chip.setTextColor(Color.WHITE)
        chip.chipCornerRadius = 10.0f
        chip.setOnCloseIconClickListener(this)
        chipGroupAdd.addView(chip)
        edt_tag.setText(dataItem.name.toString()!!)
        category=category+dataItem.name.toString()+","
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(this@AddpostActivity)
        }
        return preferenceManager
    }

    override fun onClick(v: View?) {
        chipGroupAdd.removeView(v)
        if(chipGroupSearch!=null) {
            chipGroupSearch.removeView(v)
        }
        search=0
    }

    override fun onItemLickClick(position: Int) {
        uri?.removeAt(position)
         sliderAdapter?.notifyDataSetChanged()
        addPostViewModel?.returnUri?.clear()
        for(i in uri!!.indices) {
            addPostViewModel?.returnUri?.add(uri!!.get(i))
        }
        if(uri?.indices?.isEmpty() == true){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
//        updates()
    }

    fun updates(){
        count="1"
        uri= ArrayList<Uri>()
        for (i in 0 until mArrayUri!!.size) {
            uri!!.addAll(listOf(Uri.parse(mArrayUri!!.get(i))))
        }
        activityAddPostBinding!!.imageSlider.visibility=View.VISIBLE

        filess= ArrayList()
        addPostViewModel?.returnUri?.clear()
        for (i in mArrayUri?.indices!!) {
            var uri:Uri= Uri.parse(mArrayUri!!.get(i))
            val path =
                FileUtils.getPath(this, uri)
            if(path!=null) {
                var file = File(path)
                filess?.add(file)
                addPostViewModel?.returnUri?.add(uri!!)
            }
        }

        addPostViewModel?.path?.addAll(filess!!)

        if(uri!!.size>1) {
            sliderAdapter = SliderAdapter(uri!!, this)
            activityAddPostBinding!!.imageSlider.setSliderAdapter(sliderAdapter!!)
            activityAddPostBinding!!.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
            activityAddPostBinding!!.imageSlider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
            activityAddPostBinding!!.imageSlider.startAutoCycle()
        }else{
            activityAddPostBinding!!.imageSlider.visibility=View.GONE
            activityAddPostBinding!!.img.visibility=View.VISIBLE
            activityAddPostBinding!!.img.setImageURI(uri!!.get(0))
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(search==0) {
            search()
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun onItemClick(user_id: String, name: String) {
        chipSearch1?.text =name
        chipSearch1?.setTag(user_id)
        chipSearch1?.setChipBackgroundColorResource(R.color.txt_pink)
        chipSearch1?.isCloseIconVisible = true
        chipSearch1?.setTextColor(Color.WHITE)
        chipSearch1?.chipCornerRadius = 10.0f
        chipSearch1?.setOnCloseIconClickListener(this)
        chipGroupSearch.addView(chipSearch1)
        tagID=user_id
        search=1
        activityAddPostBinding?.rvSearch?.visibility=View.GONE
    }
    private fun prepareNotificationMessage(to: ArrayList<String>, from: String, name: String) {

        val NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC
        val NOTIFICATION_TITLE = name
        val NOTIFICATION_MESSAGE = "has added a new post!!"
        val NOTIFICATION_TYPE = "notificationType_POST"

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

        } catch (e: java.lang.Exception) {
            e.message?.let { showToast(it) }
        }
        sendFcmNotification(notificationJo)
        subscribeTOTopic()

    }

    private fun subscribeTOTopic() {

        Firebase.messaging.subscribeToTopic(Constants.FCM_TOPIC)
            .addOnCompleteListener { task ->
                var msg = "R.string.msg_subscribed"
                if (!task.isSuccessful) {
                    msg =" R.string.msg_subscribe_failed"
                }
                Log.d(AppSignatureHelper.TAG, msg)
            }
    }

    private fun sendFcmNotification(notificationJo: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo,
                Response.Listener { response: JSONObject? -> },
                Response.ErrorListener {
                    Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
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
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

}