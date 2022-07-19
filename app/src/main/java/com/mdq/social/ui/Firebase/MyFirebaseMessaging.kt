package com.mdq.social.ui.Firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mdq.social.R
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.ui.livechat.LiveChatActivity
import com.mdq.social.ui.notification.NotificationActivity
import java.util.*
import kotlin.collections.ArrayList

class MyFirebaseMessaging : FirebaseMessagingService() {
    var preferenceManager1: AppPreference? = null

    private val NOTIFICATION_CHANNEL_ID =
        "MY_NOTIFICATION_CHANNEL_ID" //Required for android O and above.

    override fun onNewToken(s: String) {


        Log.i("sanjairam", s)

        preferenceManager1 = AppPreference(this@MyFirebaseMessaging)
        preferenceManager1?.FIREBASETOKEN = s
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //all notifications will be received here
        preferenceManager1 = AppPreference(this@MyFirebaseMessaging)
        //get data
        val notificationType = remoteMessage.data["notificationType"]

        if (notificationType.equals("notificationType_LIKE")) {

            val buyerUid = remoteMessage.data["to_id"]
            val sellerUid = remoteMessage.data["from_id"]
            val notificationTitle = remoteMessage.data["notificationTitle"]
            val notificationDescription = remoteMessage.data["notificationMessage"]
            //user is signed in and is same user  to which notification is to be sent
            if (preferenceManager1?.USERID.equals(buyerUid)) {
                if (!preferenceManager1?.LIKEANDCOMMENT.equals("0")) {
                    showNotification(
                        sellerUid,
                        notificationTitle,
                        notificationDescription,
                        notificationType!!
                    )
                }
            }
        }

        if (notificationType.equals("notificationType_Comment")) {

            val buyerUid = remoteMessage.data["to_id"]
            val sellerUid = remoteMessage.data["from_id"]
            val notificationTitle = remoteMessage.data["notificationTitle"]
            val notificationDescription = remoteMessage.data["notificationMessage"]
            //user is signed in and is same user  to which notification is to be sent
            if (preferenceManager1?.USERID.equals(buyerUid)) {
                if (!preferenceManager1?.USERID.equals(sellerUid)) {
                    if (!preferenceManager1?.LIKEANDCOMMENT.equals("0")) {
                        showNotification(
                            sellerUid,
                            notificationTitle,
                            notificationDescription,
                            notificationType!!
                        )
                    }
                }
            }
        }

        if (notificationType.equals("notificationType_Follow")) {
            val buyerUid = remoteMessage.data["to_id"]
            val sellerUid = remoteMessage.data["from_id"]
            val notificationTitle = remoteMessage.data["notificationTitle"]
            val notificationDescription = remoteMessage.data["notificationMessage"]
            //user is signed in and is same user  to which notification is to be sent
            if (preferenceManager1?.USERID.equals(buyerUid)) {
                if (!preferenceManager1?.FOLLOWERREQUEST.equals("0")) {
                    showNotification(
                        sellerUid,
                        notificationTitle,
                        notificationDescription,
                        notificationType!!
                    )
                }
            }
        }

        if (notificationType.equals("notificationType_Accept")) {

            val buyerUid = remoteMessage.data["to_id"]
            val sellerUid = remoteMessage.data["from_id"]
            val notificationTitle = remoteMessage.data["notificationTitle"]
            val notificationDescription = remoteMessage.data["notificationMessage"]
            //user is signed in and is same user  to which notification is to be sent
            if (preferenceManager1?.USERID.equals(buyerUid)) {
                if (!preferenceManager1?.FOLLOWERACCEPTANCE.equals("0")) {
                    showNotification(
                        sellerUid,
                        notificationTitle,
                        notificationDescription,
                        notificationType!!
                    )
                }
            }
        }


        if (notificationType.equals("notificationType_Chat")) {

//            val muteId = remoteMessage.data["muteId"]
//            var arr :List<String>?=null
//
//            val arr1 = ArrayList<String>()
//            val arr2 = ArrayList<String>()
//
//            if(muteId!=null) {
//                if (muteId!!.contains(",")) {
//                    arr = muteId!!.split(",")
//                }
//            }
//            if(arr!=null) {
//                if (arr!!.size > 1) {
//                    for (i in arr!!.indices) {
//                        arr1.add(arr.get(i).replace("]", ""))
//                        arr2.add(arr1.get(i).replace("[", ""))
//                    }
//                }
//            }else{
//                arr1.add(muteId!!.replace("]", ""))
//                arr2.add(arr1.get(0).replace("[", ""))
//            }

            val buyerUid = remoteMessage.data["to_id"]
            val sellerUid = remoteMessage.data["from_id"]
            val profile = remoteMessage.data["profile"]
            val FFrom_ID = remoteMessage.data["FFrom_id"]
            val FTO_ID = remoteMessage.data["FTo_id"]
            val notificationTitle = remoteMessage.data["notificationTitle"]
            val notificationDescription = remoteMessage.data["notificationMessage"]
            //user is signed in and is same user  to which notification is to be sent
            if (preferenceManager1?.USERID.equals(buyerUid)) {
                if (!preferenceManager1?.MESSAGE.equals("0")) {
                    if (!preferenceManager1?.LIVECHAT.equals("1")) {
//                        for (i in arr2!!.indices) {
                        showNotificationchat(
                            sellerUid,
                            notificationTitle,
                            notificationDescription,
                            notificationType!!,
                            profile, FTO_ID!!, FFrom_ID!!
                        )
//                    }
                    }
                }
            }

            if (notificationType.equals("notificationType_POST")) {
                val buyerUid = remoteMessage.data["to_id"]
                Log.i("useruserid", buyerUid!!)
                var arr: List<String>? = null

                val arr1 = ArrayList<String>()
                val arr2 = ArrayList<String>()

                if (buyerUid!!.contains(",")) {
                    arr = buyerUid!!.split(",")
                }

                if (arr != null) {
                    if (arr!!.size > 1) {
                        for (i in arr!!.indices) {
                            arr1.add(arr.get(i).replace("]", ""))
                            arr2.add(arr1.get(i).replace("[", ""))
                        }
                    }
                } else {

                    arr1.add(buyerUid.replace("]", ""))
                    arr2.add(arr1.get(0).replace("[", ""))

                }

                val sellerUid = remoteMessage.data["from_id"]
                val notificationTitle = remoteMessage.data["notificationTitle"]
                val notificationDescription = remoteMessage.data["notificationMessage"]

                //user is signed in and is same user  to which notification is to be sent

                if (!preferenceManager1?.POSTS.equals("0")) {

                    if (arr2 != null) {
                        for (i in arr2!!.indices) {
                            if (preferenceManager1?.USERID!! != arr2.get(i).trim()) {
                                showNotification(
                                    sellerUid,
                                    notificationTitle,
                                    notificationDescription,
                                    notificationType!!
                                )
                            }
                        }
                    } else {
                        showNotification(
                            sellerUid,
                            notificationTitle,
                            notificationDescription,
                            notificationType!!
                        )
                    }
                }
            }
        }
    }

    private fun showNotification(
        sellerUid: String?,
        notificationTitle: String?,
        notificationDescription: String?,
        notificationType: String
    ) {
        //notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //id for notification, random
        val notificationID = Random().nextInt(3000)

        //check if android version is Oreo/O or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel(notificationManager)
        }

        var intent: Intent? = null
        if (notificationType == "notificationType_LIKE") {
            intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("orderBy", sellerUid) //user id
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        } else if (notificationType == "notificationType_Comment") {
            intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("orderTo", sellerUid) // merchant id
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        } else if (notificationType == "notificationType_Follow") {
            intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("orderTo", sellerUid) // merchant id
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        } else if (notificationType == "notificationType_Accept") {
            //open OrderDetailsUserActvity
            intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("orderTo", sellerUid) // merchant id
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        } else if (notificationType == "notificationType_POST") {
            //open OrderDetailsUserActvity
            intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("orderTo", sellerUid) // merchant id
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        } else if (notificationType == "notificationType_Chat") {
            //open OrderDetailsUserActvity
            intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("orderTo", sellerUid) // merchant id
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //Large Icon
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_logo)

        //sound of notification
        val notificationSounUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(largeIcon)
            .setContentTitle(notificationTitle + " " + notificationDescription)
            .setSound(notificationSounUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        //show notification
        notificationManager.notify(notificationID, notificationBuilder.build())

    }

    private fun showNotificationchat(
        sellerUid: String?,
        notificationTitle: String?,
        notificationDescription: String?,
        notificationType: String,
        profile: String?, FTo_id: String, FFrom_id: String
    ) {

        //notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //id for notification, random
        val notificationID = Random().nextInt(3000)

        //check if android version is Oreo/O or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel(notificationManager)
        }

        var intent: Intent? = null
        if (notificationType == "notificationType_Chat") {
            //open OrderDetailsUserActvity
            intent = Intent(this, LiveChatActivity::class.java)

            intent.putExtra("to_id", sellerUid)
            intent.putExtra("name", notificationTitle)
            intent.putExtra("who", "noti")
            intent.putExtra(
                "image",
                profile
            )


            intent.putExtra("ToFireBaseID", FFrom_id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //Large Icon
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_logo)

        //sound of notification
        val notificationSounUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.drawable.ic_logo)
            .setLargeIcon(largeIcon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationDescription)
            .setSound(notificationSounUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)


        //show notification
        notificationManager.notify(notificationID, notificationBuilder.build())

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannel(notificationManager: NotificationManager?) {
        val channelName: CharSequence = "Some Sample Text"
        val channelDescription = "Channel Description here"
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = channelDescription
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(notificationChannel)

    }

}

