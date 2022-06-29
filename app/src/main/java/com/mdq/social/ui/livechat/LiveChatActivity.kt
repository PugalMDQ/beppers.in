package com.mdq.social.ui.livechat

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.response.chatBlockStatus.ChatBlockedStatus
import com.mdq.social.app.data.response.livechat.LiveChatResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.livechat.LiveChatViewModel
import com.mdq.social.app.helper.appsignature.AppSignatureHelper.Companion.TAG
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityLiveChatBinding
import com.mdq.social.ui.Firebase.Constants
import com.mdq.social.ui.chat.ChatFragment
import com.mdq.social.ui.home.HomeActivity
import com.mdq.social.ui.models.ChatMessage
import com.mdq.social.ui.profile.ProfileActivity
import com.mdq.social.utils.DateUtils.getFormattedTimeChatLog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_live_chat.*
import kotlinx.android.synthetic.main.dialog_logout.*
import kotlinx.android.synthetic.main.item_chat.view.*
import org.json.JSONObject


class LiveChatActivity : BaseActivity<ActivityLiveChatBinding, LiveChatNavigator>(),
    LiveChatNavigator {

    private var activityLiveChatBinding: ActivityLiveChatBinding? = null
    private var liveChatViewModel: LiveChatViewModel? = null
    var TO: String = ""
    var Blockedid: String = ""
    var ToFireBaseID: String = ""
    var ii: Int = 0
    var fromUser: String = ""
    var image: String = ""
    var who: String = ""
    var name: String? = null
    var liveChatAdapter: LiveChatAdapter? = null
    var preferenceManager: PreferenceManager? = null
    val adapter = GroupAdapter<ViewHolder>()
    var frag: ChatFragment = ChatFragment()
    var muteList = ArrayList<String>()
    val cm = ArrayList<String>()
    private val latestMessagesMap = HashMap<String, ChatMessage>()
    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100
    var Blocked: Boolean = true
    var cc: ChatMessage? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_live_chat
    }

    override fun getViewBindingVarible(): Int {
        return BR.liveChatViewModel
    }

    override fun getViewModel(): BaseViewModel<LiveChatNavigator> {
        liveChatViewModel =
            ViewModelProvider(this, viewModelFactory).get(LiveChatViewModel::class.java)
        return liveChatViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLiveChatBinding = getViewDataBinding()

//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE);

        name = intent.getStringExtra("name").toString()
        TO = intent.getStringExtra("to_id").toString()
        Blockedid = intent.getStringExtra("Blockedid").toString()
        image = intent.getStringExtra("image").toString()
        ToFireBaseID = intent.getStringExtra("ToFireBaseID").toString()

        if (TO.trim().equals(Blockedid.trim())) {
            activityLiveChatBinding?.Mute?.setImageDrawable(resources.getDrawable(R.drawable.ic_mo_unblockmsg))
            Blocked = false
            activityLiveChatBinding?.edtMessage?.visibility = View.GONE
            activityLiveChatBinding?.imageView?.visibility = View.GONE
            activityLiveChatBinding?.imgSend?.visibility = View.GONE
        } else {
            activityLiveChatBinding?.Mute?.setImageDrawable(resources.getDrawable(R.drawable.ic_mo_blockmsg))
            Blocked = true
        }

        try {
            who = intent.getStringExtra("who").toString()
        } catch (e: Exception) {

        }

        activityLiveChatBinding!!.Mute.setOnClickListener {
            val dialog = Dialog(this, R.style.dialog_center)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_block)
            dialog.show()
            val textView23 = dialog.findViewById<TextView>(R.id.textView23)
            val textView22 = dialog.findViewById<TextView>(R.id.textView22)
            val textView24 = dialog.findViewById<TextView>(R.id.textView24)

            if (Blocked) {
                textView22.setText("Do you want to Block " + name + " ?")
            } else {
                textView22.setText("Do you want to UnBlock " + name + " ?")
            }

            textView23.setOnClickListener {
                dialog.dismiss()
            }
            textView24.setOnClickListener {
                dialog.dismiss()
                if (Blocked) {
                    block(TO)
                } else {
                    unblock(TO)
                }
            }
        }

        //ChatSeenCall
        chatSeenCall()
        //getListChat
//        getListChat()

        listenForMessages(1, "acdsc")

        activityLiveChatBinding?.ProfilePic?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java).putExtra("id", TO))
        }

        activityLiveChatBinding?.rvChat?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    println("Scrolled Downwards")
//                    Loop(1000)
                } else if (dy < 0) {
                    println("Scrolled Upwards")
//                    Loop(0)
                } else {
                    println("No Vertical Scrolled")
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_FLING -> println("SCROLL_STATE_FLING")
                    AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> println("SCROLL_STATE_TOUCH_SCROLL")
                    else -> {

                    }
                }
            }
        })

        if (!image.isNullOrEmpty()) {
            if (!image.contains("null")) {
                Glide.with(this)
                    .load(image).into(activityLiveChatBinding!!.ProfilePic)
            }
        }

        activityLiveChatBinding?.imgBack?.setOnClickListener {

            onBackPressed()
//            frag.backcall()
        }

        activityLiveChatBinding?.Menu?.setOnClickListener {
            if (ii == 0) {
                ii = 1
                activityLiveChatBinding?.Mute?.visibility = View.VISIBLE
            } else if (ii == 1) {
                ii = 0
                activityLiveChatBinding?.Mute?.visibility = View.GONE
            }
        }

        appPreference.LIVECHAT = "1"

        fromUser = appPreference.FIREBASEUSERID

        activityLiveChatBinding!!.textView75.setText(name)

        activityLiveChatBinding?.rvChat?.adapter = adapter

        activityLiveChatBinding?.imgSend?.setOnClickListener {
            val ll = activityLiveChatBinding?.edtMessage?.text.toString()

            if (TextUtils.isEmpty(activityLiveChatBinding?.edtMessage?.text.toString())) {
                showToast("please enter message")
                return@setOnClickListener
            } else {
                performSendMessage()
            }
            if (ll != null) {
                if (!TO.isNullOrEmpty()) {
                    liveChatViewModel!!.InsertLiveChat(
                        TO,
                        ll
                    ).observe(this, Observer { response ->
                        activityLiveChatBinding?.edtMessage?.setText("")
                        if (response?.data != null) {
                            val response = response.data as SignupResponse
                            if (response.message.equals("Latest chat updated successfully")) {
//                                getListChat()
                            }
                        } else {
                            showToast(response.throwable?.message!!)
                        }
                    })
                }
            }
        }
    }

    fun block(id: String) {
        liveChatViewModel!!.ChatBlock(
            id
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val followResponse = response.data as SignupResponse
                if (followResponse.message.equals("chat has been blocked!")) {
                    Blocked = false
                    startActivity(Intent(this@LiveChatActivity, HomeActivity::class.java))
                }
            }
        })
    }

    fun unblock(id: String) {
        liveChatViewModel!!.ChatUnBlock(
            id
        ).observe(this, Observer { response ->
            if (response?.data != null) {
                val followResponse = response.data as SignupResponse
                if (followResponse.message.equals("chat has been Unblocked!")) {
                    Blocked = true
                    startActivity(Intent(this@LiveChatActivity, HomeActivity::class.java))
                }
            }
        })
    }

    private fun Loop(a: Int) {
        for (i in 0 until a) {
            Handler(Looper.getMainLooper()).postDelayed({

                getListChat()

            }, 3000)
        }
    }

    private fun chatSeenCall() {
        liveChatViewModel!!.ChatSeenCall(TO).observe(this, Observer { response ->
            if (response?.data != null) {
            }
        })
    }

    public fun listenForMessages(postion: Int, who: String) {
        try {
            adapter.clear()
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {

        }
        Log.i("fromUser", appPreference.FIREBASEUSERID)
        Log.i("TOUSER", ToFireBaseID)
        val fromId = appPreference.FIREBASEUSERID ?: return
        val toId = ToFireBaseID ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "database error: " + databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "has children: " + dataSnapshot.hasChildren())
                if (!dataSnapshot.hasChildren()) {

                }
            }
        })

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(
                dataSnapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                    cm?.add(it.id)
                    adapter.add(
                        ChatFromItem(
                            it.text,
                            it.fromId,
                            it.toId,
                            it.timestamp,
                            appPreference.FIREBASEUSERID,
                            toId,
                            cm,
                            this@LiveChatActivity,
                            this@LiveChatActivity
                        )
                    )
                }
                if (who.equals("inner")) {
                    rv_chat.scrollToPosition(postion - 1)
                } else {
                    rv_chat.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }
        })
    }

    private fun performSendMessage() {

        val text = edt_message.text.toString()
        edt_message.text.clear()

        val fromId = appPreference.FIREBASEUSERID
        val toId = ToFireBaseID.trim()

        Log.i("frmo", appPreference.FIREBASEUSERID)
        Log.i("to", ToFireBaseID)
        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage =
            ChatMessage(fromId, reference.key!!, text, System.currentTimeMillis() / 1000, toId)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                prepareNotificationMessage(
                    TO,
                    appPreference.USERID,
                    appPreference.USERNAME!!,
                    text,
                    toId
                )
            }
        toReference.setValue(chatMessage)

        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }


    private fun getListChat() {
        liveChatViewModel!!.getLiveChatList(TO).observe(this, Observer { response ->
            if (response?.data != null) {
                val response = response.data as LiveChatResponse
                if (response.data != null) {
                    var size = response.data.size
//                    liveChatAdapter = LiveChatAdapter(this, response.data, appPreference.USERID, TO)
//                    activityLiveChatBinding?.rvChat?.adapter = liveChatAdapter
//                    activityLiveChatBinding?.rvChat?.scrollToPosition(size - 1)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(this@LiveChatActivity)
        }
        return preferenceManager
    }

    private fun prepareNotificationMessage(
        to: String,
        from: String,
        name: String,
        message: String,
        FTO_ID: String
    ) {

        val NOTIFICATION_TOPIC = "/topics/" + Constants.FCM_TOPIC
        val NOTIFICATION_TITLE = appPreference.USERNAME
        val NOTIFICATION_MESSAGE = message
        val NOTIFICATION_TYPE = "notificationType_Chat"

        //prepare json
        val notificationJo = JSONObject()
        val notificationBodyJo = JSONObject()

        try {
            //what to send
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE)
            notificationBodyJo.put("to_id", to)
            notificationBodyJo.put("profile", appPreference.PROFILE)
            notificationBodyJo.put("from_id", from)
            notificationBodyJo.put("notificationTitle", NOTIFICATION_TITLE)
            notificationBodyJo.put("notificationMessage", NOTIFICATION_MESSAGE)
            notificationBodyJo.put("FFrom_id", appPreference.FIREBASEUSERID)
            notificationBodyJo.put("FTo_id", FTO_ID)

            Log.i("sasa", FTO_ID)
            Log.i("frfrf", appPreference.FIREBASEUSERID)
            //where to send
            notificationJo.put("to", NOTIFICATION_TOPIC) //to all who subscribed
            notificationJo.put("data", notificationBodyJo)

        } catch (e: Exception) {
            e.message?.let { showToast(it) }
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
                    Toast.makeText(
                        this, "" + it.message, Toast.LENGTH_SHORT
                    ).show()
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


    override fun onBackPressed() {
        if (who.equals("noti")) {
            startActivity(Intent(this@LiveChatActivity, HomeActivity::class.java))
        } else {
            super.onBackPressed()
        }
        appPreference.LIVECHAT = "0"
    }

    override fun onStop() {
        super.onStop()
        appPreference.LIVECHAT = "0"
    }

    override fun onPause() {
        super.onPause()
        appPreference.LIVECHAT = "0"
    }

    class ChatFromItem(
        val text: String,
        val Ffromif: String,
        val FToid: String,
        var timestamp1: Long,
        val from: String,
        val to: String,
        var chatMessage: ArrayList<String>?,
        var context: Context,
        var ll: LiveChatNavigator
    ) : Item<ViewHolder>() {

        override fun bind(viewHolder: ViewHolder, position: Int) {

            if (from.trim().equals(Ffromif.trim())) {
                viewHolder.itemView.textView68.visibility = View.VISIBLE
                viewHolder.itemView.FROM_DATE.visibility = View.VISIBLE
                viewHolder.itemView.FROM_DATE.setText(getFormattedTimeChatLog(timestamp1))
                viewHolder.itemView.TO_DATE.visibility = View.GONE
                viewHolder.itemView.textView72.visibility = View.GONE
                viewHolder.itemView.textView68.text = text
            } else if (to.trim().equals(Ffromif.trim())) {
                viewHolder.itemView.textView68.visibility = View.GONE
                viewHolder.itemView.FROM_DATE.visibility = View.GONE
                viewHolder.itemView.textView72.visibility = View.VISIBLE
                viewHolder.itemView.TO_DATE.visibility = View.VISIBLE
                viewHolder.itemView.TO_DATE.setText(getFormattedTimeChatLog(timestamp1))
                viewHolder.itemView.textView72.text = text
            } else {
                viewHolder.itemView.textView68.visibility = View.GONE
                viewHolder.itemView.FROM_DATE.visibility = View.GONE
                viewHolder.itemView.textView72.visibility = View.GONE
                viewHolder.itemView.TO_DATE.visibility = View.GONE
            }

            viewHolder.itemView.textView68.setOnLongClickListener {
                val dialoglogout = Dialog(context, R.style.dialog_center)
                dialoglogout.setCancelable(false)
                dialoglogout.setContentView(R.layout.dialog_logout)
                dialoglogout.show()
                val textView23 = dialoglogout.textView23
                val textView21 = dialoglogout.textView21
                val textView22 = dialoglogout.textView22
                val textView24 = dialoglogout.textView24

                textView21.setText("Delete")
                textView22.setText("Do you want to delete?")

                textView23.setOnClickListener {
                    dialoglogout.dismiss()
                }

                Toast.makeText(context, "" + position, Toast.LENGTH_LONG).show()
                textView24.setOnClickListener {
                    deleteChats(chatMessage!!.get(position), position)
                    dialoglogout.dismiss()
                }

                true
            }
            val fragment = ChatFragment()
            fragment.textview1?.setText(text)
        }

        public fun deleteChats(deleteID: String, position: Int) {
            val data = FirebaseDatabase.getInstance().reference.child("user-messages")
                .child(from.trim()).child(to.trim()).child(deleteID.trim())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            data.ref.removeValue()
                        }
                        Handler().postDelayed({
                            ll.generateListCall(position)
                        }, 600)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
        }

        override fun getLayout(): Int {
            return R.layout.item_chat
        }
    }

    override fun generateListCall(position: Int) {
        listenForMessages(position, "inner")
    }
}

