package com.mdq.social.ui.notification

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.response.Rating.Ratings
import com.mdq.social.app.data.response.addlikecomments.AddLikeCommentsResponse
import com.mdq.social.app.data.response.bookmarklist.BookMarkListResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.DataItemes
import com.mdq.social.app.data.response.getshopAlbumDetails.NewPostsearchDeailResponse
import com.mdq.social.app.data.response.notification.NotificationListResponse
import com.mdq.social.app.data.response.recent.DataItem
import com.mdq.social.app.data.response.recent.RecentResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.notification.NotificationViewModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.FragmentNotificationBinding
import com.mdq.social.ui.Firebase.Constants
import com.mdq.social.ui.adapters.AdapterForBookmark
import com.mdq.social.ui.adapters.AdapterForSearchPost
import com.mdq.social.ui.adapters.AdapterForTrendingPost
import com.mdq.social.ui.listOfPost.AdapterForList
import com.mdq.social.ui.search.TrendingAdapter
import com.mdq.social.ui.searchdetails.VideoAdapter
import kotlinx.android.synthetic.main.fragment_notification.*
import org.json.JSONObject
import java.lang.reflect.Type

class NotificationActivity : BaseActivity<FragmentNotificationBinding, NotificationNavigator>(),
    NotificationNavigator,TrendingAdapter.postClick,AdapterForList.like,AdapterForTrendingPost.ClickManager,AdapterForSearchPost.like,VideoAdapter.postClick,AdapterForList.clickManager,NotificationAdapter.readcall,AdapterForBookmark.like{
    private var notificationViewModel: NotificationViewModel? = null
    private var fragmentNotificationBinding: FragmentNotificationBinding? = null
    private var notificationAdapter: NotificationAdapter? = null
    private var adapterForTrendingPost: AdapterForTrendingPost? = null
    private var adapterForSearchPost: AdapterForSearchPost? = null
    private var adapterForBookmark: AdapterForBookmark? = null
    private var galleryAdapter: VideoAdapter? = null
    private var from: String? = ""
    private var fromBook: String? = ""
    private var fromPost: String? = ""
    private var search: String? = ""
    private var city: String? = ""
    private var area: String? = ""
    private var category: String? = ""
    private var fromPostsearch: String? = ""
    private var fromPostfilter: String? = ""
    private var position: Int? =null
    private var User_id: String? = ""
    var linearLayoutManager: LinearLayoutManager?=null
    var adapterForList: AdapterForList?=null
    private var preferenceManager: PreferenceManager?=null
    var trendingItem:List<DataItem>?=null
    var userProfileResponses:UserProfileResponse?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_notification
    }

    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }
    }

    override fun getViewModel(): BaseViewModel<NotificationNavigator> {
        notificationViewModel =
            ViewModelProvider(this, viewModelFactory).get(NotificationViewModel::class.java)
        return notificationViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.notificationViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        img_back.setOnClickListener {
            finish()
        }
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);


        linearLayoutManager= LinearLayoutManager(this)
        fragmentNotificationBinding = getViewDataBinding()
        fragmentNotificationBinding?.notificationViewModel = notificationViewModel
        notificationViewModel?.navigator = this


        img_back.setOnClickListener {
            onBackPressed()
        }
        fragmentNotificationBinding?.back?.setOnClickListener {
            onBackPressed()
        }

        var listPrivate: List<DataItemes>? = ArrayList()

        val type: Type = object : TypeToken<List<DataItemes?>?>() {}.getType()

        try {
            from = intent!!.extras!!.getString("ProfileAdapter").toString()
            fromBook = intent!!.extras!!.getString("BookmarkAdapter")
            fromPost = intent!!.extras!!.getString("PostAdapter").toString()
            fromPostsearch = intent!!.extras!!.getString("SearchPost").toString()
            fromPostfilter = intent!!.extras!!.getString("PostFilterAdapter").toString()
            position = intent!!.extras!!.getInt("position")
            search=intent!!.extras!!.getString("search")
            city=intent!!.extras!!.getString("city")
            area=intent!!.extras!!.getString("area")
            category=intent!!.extras!!.getString("category")
            User_id = intent!!.extras!!.getString("user_id").toString()
            listPrivate = Gson().fromJson(intent.getStringExtra("private_list"), type)
        }catch (e:Exception){

        }

        if(from!!.equals("ProfileAdapter")){
            fragmentNotificationBinding?.title?.visibility=View.VISIBLE
            fragmentNotificationBinding?.back?.visibility=View.VISIBLE
            fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
            fragmentNotificationBinding?.cons?.visibility=View.GONE
            fragmentNotificationBinding?.textView24?.visibility=View.GONE
            img_back.visibility=View.GONE
            getProfileDetail(User_id!!)
        }
        else if(fromBook.equals("BookmarkAdapter")){
            getBookmark()
            fragmentNotificationBinding?.title?.visibility=View.VISIBLE
            fragmentNotificationBinding?.title?.setText("Bookmark")
            fragmentNotificationBinding?.back?.visibility=View.VISIBLE
            fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
            fragmentNotificationBinding?.cons?.visibility=View.GONE
            img_back.visibility=View.GONE
            fragmentNotificationBinding?.textView24?.visibility=View.GONE
        }
        else if (fromPost.equals("PostAdapter")){
            fragmentNotificationBinding?.title?.visibility=View.VISIBLE
            fragmentNotificationBinding?.back?.visibility=View.VISIBLE
            fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
            fragmentNotificationBinding?.cons?.visibility=View.GONE
            fragmentNotificationBinding?.textView24?.visibility=View.GONE
            img_back.visibility=View.GONE
            getRecent(appPreference.USERID)
        }else if (fromPostfilter.equals("PostFilterAdapter")){

            fragmentNotificationBinding?.title?.visibility=View.VISIBLE
            fragmentNotificationBinding?.back?.visibility=View.VISIBLE
            fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
            fragmentNotificationBinding?.cons?.visibility=View.GONE
            fragmentNotificationBinding?.textView24?.visibility=View.GONE
            img_back.visibility=View.GONE
            if(category!=null ||city!=null ||area!=null){
                filterPost()
            }else if(search!=null){
                serach()
            }
        }else{
            fetchnotification()
        }
    }

    private fun getBookmark() {
        notificationViewModel!!.getBookmark()
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val bookMarkListResponse = response.data as BookMarkListResponse
                    if (bookMarkListResponse != null && bookMarkListResponse?.data != null) {
                        if (bookMarkListResponse.data.size != 0) {
                            adapterForBookmark = AdapterForBookmark(this@NotificationActivity,bookMarkListResponse.data,position,this,appPreference.USERID,"Bookmark")
                            fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
                            fragmentNotificationBinding?.rv?.adapter=adapterForBookmark
                            fragmentNotificationBinding?.rv?.layoutManager=linearLayoutManager
                            fragmentNotificationBinding?.rv?.scrollToPosition(position!!)
                        } else {
                            fragmentNotificationBinding?.rv?.visibility=View.GONE
                        }
                    } else {
                        fragmentNotificationBinding?.rv?.visibility=View.GONE
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    private fun fetchnotification() {

        notificationViewModel!!.getNotificationList(appPreference.USERID).observe(this, Observer { response ->
            if (response?.data != null) {
                val Response = response.data as NotificationListResponse
                if (Response.data != null) {
                    notificationAdapter = NotificationAdapter(this,Response.data,this)
                    fragmentNotificationBinding?.notificationAdapter = notificationAdapter
                } else {
                    Response.message?.let { showToast(it) }
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun serach() {

        notificationViewModel!!.getSearchDetails(
            search!!
        )
            .observe(this, Observer { response ->
                if (response?.data != null) {

                    val getShopAlbumDetailsResponse =
                        response.data as NewPostsearchDeailResponse
                    if (getShopAlbumDetailsResponse.data != null) {

                        var data=ArrayList<DataItemes>()
                        for(i in getShopAlbumDetailsResponse?.data!!.indices){
                            if(!getShopAlbumDetailsResponse?.data.get(i).user_id.equals(appPreference.USERID)){
                                data.add(getShopAlbumDetailsResponse?.data.get(i))
                            }
                        }
                        adapterForSearchPost=
                            AdapterForSearchPost(this,data!!,position,this,appPreference.USERID,"search")

                        fragmentNotificationBinding?.rv?.layoutManager=linearLayoutManager

                           fragmentNotificationBinding?.rv?.adapter =
                            adapterForSearchPost
                        fragmentNotificationBinding?.rv?.scrollToPosition(position!!)
                        adapterForSearchPost!!.notifyDataSetChanged()

                    }

                    } else{
                            showToast("No results found")
                        }
            })
    }

    private fun amIConnected(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getRecent(userId: String) {
        notificationViewModel!!.getRecent(userId).observe(this, Observer { response ->
            if (response?.data != null) {
                val recentResponse = response.data as RecentResponse
                if (recentResponse != null && recentResponse?.data != null) {

                    var dataes = ArrayList<DataItem>()

                    for(i in recentResponse?.data!!.indices){
                        if(!appPreference.USERID.equals(recentResponse?.data!!.get(i).user_id?.trim()))
                            dataes.add(recentResponse?.data!!.get(i))
                    }
                    trendingItem=recentResponse.data
                    adapterForTrendingPost = AdapterForTrendingPost(this@NotificationActivity, dataes!!,position!!,this,appPreference.USERID)
                    fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
                    fragmentNotificationBinding?.rv?.adapter=adapterForTrendingPost
                    fragmentNotificationBinding?.rv?.layoutManager=linearLayoutManager
                    adapterForTrendingPost!!.notifyItemChanged(position!!)
                    fragmentNotificationBinding?.rv?.scrollToPosition(position!!)

                } else {
                    recentResponse.message?.let { showToast(it) }
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun getProfileDetail(user_id: String) {
        notificationViewModel!!.getUserProfile(
            user_id
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val UserProfileResponses = response.data as UserProfileResponse
                if(UserProfileResponses!=null) {
                    userProfileResponses=UserProfileResponses
                    adapterForList = AdapterForList(this@NotificationActivity, UserProfileResponses,position!!,this,user_id,appPreference.USERID,this)
                    fragmentNotificationBinding?.rv?.visibility=View.VISIBLE
                    fragmentNotificationBinding?.rv?.adapter=adapterForList
                    fragmentNotificationBinding?.rv?.layoutManager=linearLayoutManager
                    adapterForList!!.notifyItemChanged(position!!)
                    fragmentNotificationBinding?.rv?.scrollToPosition(position!!)
                }
            }
        })
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(this)
        }
        return preferenceManager
    }

    override fun onItemLickClicksOfProfile(
        id: String,
        gallery: String,
        user_id: String,
        position: Int
    ) {

    }

    override fun like(id: String,user_id: String,image:ImageView,no_of_like:String,positions: Int,active:String,textViews: TextView?) {

        position=positions
        if(image.getTag().equals("Unliked")) {
            notificationViewModel!!.AddLike(
                id.toString(),
                user_id.toString()
            )
                .observe(this, Observer { response ->
                    if (response?.data != null) {
                        val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                        if (addLikeCommentsResponse != null) {
                            if (addLikeCommentsResponse.message.equals("Like added successfully!")) {
                                image.setImageResource(R.drawable.ic_heart_1fill)

                                var i = textViews?.text.toString().toInt()
                                i=i+1
                                textViews?.setText(i.toString())
                                image.setTag("Liked")

                                prepareNotificationMessage( user_id.toString(),appPreference.USERID,appPreference.NAME)

                                insertNotification( id.toString(),user_id,appPreference.NAME)

                            }
                        }

                    }
                })
        }
        else if(image.getTag().equals("Liked")) {
                notificationViewModel!!.UnLike(
                    id.toString(),
                    user_id.toString(),
                )
                    .observe(this, Observer { response ->
                        if (response?.data != null) {
                            val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                            if (addLikeCommentsResponse != null) {
                                if (addLikeCommentsResponse.message.equals("Like removed successfully!")) {
                                    image.setImageResource(R.drawable.ic_heart_1__1_)
                                    image.setTag("Unliked")

                                    var i = textViews?.text.toString().toInt()
                                    i=i-1
                                    if(!i.toString().contains("-")) {
                                        textViews?.setText(i.toString())
                                    }

                                }
                            }
                        }
                    })
        }
    }

    override fun onItemLickClick(
        positions: Int,
        imageView32: ImageView,
        tvLikeCount: TextView,
        recentItem: DataItem,
        active: String,
        no_of_like: String,
        textViews:TextView?
    ) {

        position=positions
        if(imageView32.getTag().equals("Unliked")) {
            notificationViewModel!!.AddLike(
                recentItem.id.toString(),
                recentItem.user_id.toString(),
            )
                .observe(this, Observer { response ->
                    if (response?.data != null) {
                        val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                        if (addLikeCommentsResponse != null) {
                            if (addLikeCommentsResponse.message.equals("Like added successfully!")) {


                                var i = textViews?.text.toString().toInt()
                                i=i+1
                                textViews?.setText(i.toString())

                                imageView32.setImageResource(R.drawable.ic_heart_1fill)
                                imageView32.setTag("Liked")

                                prepareNotificationMessage( recentItem.user_id.toString(),appPreference.USERID,appPreference.USERNAME)
                                insertNotification(recentItem. id.toString(),recentItem.user_id!!,appPreference.USERNAME)
//                                getRecent(appPreference.USERID)
                            }
                        }
                    }
                })
        }
        else if(imageView32.getTag().equals("Liked")) {

                notificationViewModel!!.UnLike(
                    recentItem.id.toString(),
                    recentItem.user_id.toString(),
                )
                    .observe(this, Observer { response ->
                        if (response?.data != null) {
                            val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                            if (addLikeCommentsResponse != null) {
                                if (addLikeCommentsResponse.message.equals("Like removed successfully!")) {
                                    imageView32.setTag("Unliked")
                                    var i = textViews?.text.toString().toInt()
                                    i=i-1
                                    if(!i.toString().contains("-")) {
                                        textViews?.setText(i.toString())
                                    }
                                    imageView32.setImageResource(R.drawable.ic_heart_1__1_)
//                                    getRecent(appPreference.USERID)
                                }
                            }
                        }
                    })
        }
    }

    override fun saveBookmark(postid: String, position: Int, userId: String?,who:String) {

        notificationViewModel!!.SaveBookmark(postid).observe(this, Observer { response ->
            if (response?.data != null) {
                if(who.equals("Bookmark")) {
                    getBookmark()
                }

            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onMenuClicked(position: Int, user_id: String, star: TextView) {

        notificationViewModel!!.getRating(user_id!!).observe(this, Observer { response ->

            val rating = response.data as Ratings
            if (rating.data != null) {
                star.setText(rating.data.get(position).avg_rating)

            } else {
                star.setText("0.00")
            }
        })
    }

    override fun onMenuClicks(position: Int, user_id: String, star: TextView) {

        notificationViewModel!!.getRating(user_id!!).observe(this, Observer { response ->

            val rating = response.data as Ratings
            if (rating.data != null) {
                star.setText("%.2f".format(rating.data.get(0).avg_rating!!.toDouble()))
            } else {
                star.setText("0.00")
            }
        })
    }

    override fun likeFromSearchPost(
        id: String,user_id: String,
        image: ImageView,
        no_of_like: String,
        positions: Int,
        active: String,
        textViews :TextView?
    ) {
        position=positions
        if(image.getTag().equals("Unliked")) {
            notificationViewModel!!.AddLike(
                id.toString(),
                user_id
            )
                .observe(this, Observer { response ->
                    if (response?.data != null) {
                        val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                        if (addLikeCommentsResponse != null) {
                            if (addLikeCommentsResponse.message.equals("Like added successfully!")) {

                                var i = textViews?.text.toString().toInt()
                                i=i+1
                                textViews?.setText(i.toString())

                                image.setImageResource(R.drawable.ic_heart_1fill)
                                prepareNotificationMessage(user_id.toString(),appPreference.USERID,appPreference.USERNAME)
                                insertNotification(id.toString(),user_id,appPreference.USERNAME)

//                                if(search!=null){
//                                    serach()
//                                }else if(category!=null ||city!=null ||area!=null){
//                                    filterPost()
//                                }
                                image.setTag("Liked")

                            }
                        }
                    }
                })
        }
        else if(image.getTag().equals("Liked")) {

                notificationViewModel!!.UnLike(
                    id.toString(),
                    user_id
                )
                    .observe(this, Observer { response ->
                        if (response?.data != null) {
                            val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                            if (addLikeCommentsResponse != null) {
                                if (addLikeCommentsResponse.message.equals("Like removed successfully!")) {

                                    var i = textViews?.text.toString().toInt()
                                    i=i-1
                                    if(!i.toString().contains("-")) {
                                        textViews?.setText(i.toString())
                                    }
                                    image.setImageResource(R.drawable.ic_heart_1__1_)
//                                    if(search!=null){
//                                        serach()
//                                    }else if(category!=null ||city!=null ||area!=null){
//                                        filterPost()
//                                    }
                                    image.setTag("Unliked")

                                }
                            }
                        }
                    })
        }
    }

    override fun hide(postid: String,who:String) {
        notificationViewModel!!.getHidePost(postid).observe(this, Observer { response ->
            if (response?.data != null) {
                val response = response.data as SignupResponse
                if (response.message.equals("hide added successfully!")) {
                    showToast("Post hidden successfully!")

                    if(who.equals("search")){
                        serach()
                    }else if(who.equals("filter")){
                        filterPost()
                    }else if(who.equals("Trending")){
                        getRecent(appPreference.USERID)
                    }

                } else {
                    response.message?.let { showToast(it) }
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    override fun onMenuClick(position: Int, userId: String?, star: TextView) {
        notificationViewModel!!.getRating(userId!!).observe(this, Observer { response ->

            val rating = response.data as Ratings
            if (rating.data != null) {
                star.setText(rating.data.get(position).avg_rating)

            } else {
                star.setText("0.00")
            }
        })
    }


    private fun filterPost(){

        notificationViewModel!!.getSearchFilterPostDetails(
            city!!,
            area!!,
            category!!.trim()
        )
            .observe(this, Observer { response ->
                if (response?.data != null) {
                    val getShopAlbumDetailsResponse =
                        response.data as NewPostsearchDeailResponse
                    fragmentNotificationBinding?.rv?.layoutManager=linearLayoutManager

                    var data=ArrayList<DataItemes>()
                    for(i in getShopAlbumDetailsResponse?.data!!.indices){
                        if(!getShopAlbumDetailsResponse?.data.get(i).user_id.equals(appPreference.USERID)){
                            data.add(getShopAlbumDetailsResponse?.data.get(i))
                        }
                    }
                    adapterForSearchPost=
                        AdapterForSearchPost(this,data,position,this,appPreference.USERID,"filter")
                    fragmentNotificationBinding?.rv?.adapter =
                            adapterForSearchPost
                    fragmentNotificationBinding?.rv?.scrollToPosition(position!!)
                    adapterForSearchPost!!.notifyDataSetChanged()

                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }

    override fun onItemLickClicksOfProfile(videoItem: List<DataItemes>?, position: Int) {

    }

    override fun enter() {

    }

    override fun delete(postid: String) {
        notificationViewModel!!.deletePost(
            postid
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val signupResponse = response.data as SignupResponse
                if(signupResponse.message.equals("post has been deleted!")) {
                    getProfileDetail(User_id!!)
                    showToast("post has been deleted!")
                }else{
                    showToast(signupResponse.message!!)
                }
            }
        })
    }

    private fun prepareNotificationMessage(to:String,from: String, name: String) {

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

        } catch (e: java.lang.Exception) {

            e.message?.let { showToast(it) }
        }
        sendFcmNotification(notificationJo)
        subscribeTOTopic(to,from,NOTIFICATION_TOPIC)

    }

    private fun subscribeTOTopic(buyerId: String, sellerId: String, notificationTopic: String) {

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

    private fun insertNotification(postId: String, toID: String,name:String) {
        notificationViewModel?.insertNotification(postId,toID,name)?.observe(this, Observer { response ->
            if (response?.data != null) {
                val res = response.data as SignupResponse
                if (res.message.equals("Notification added successfully")) {

                }else{
                    res.message?.let { showToast(it) }
                }
            }
        })
    }

    override fun readcall(id: String) {

        notificationViewModel?.readNotificationCall(id)?.observe(this, Observer { response ->
            if (response?.data != null) {
                val res = response.data as SignupResponse
                if (res.message.equals("Notification read updated successfully")) {
                }else{
                    res.message?.let { showToast(it) }
                }
            }
        })
    }

}