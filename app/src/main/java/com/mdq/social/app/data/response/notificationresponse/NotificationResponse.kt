package com.mdq.social.app.data.response.notificationresponse

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("received_notification_status")
	val receivedNotificationStatus: String? = null,

	@field:SerializedName("likes_comments_notification")
	val likesCommentsNotification: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("follower_accept_notification")
	val followerAcceptNotification: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("chat_message_notification")
	val chatMessageNotification: String? = null,

	@field:SerializedName("video_images_notification")
	val videoImagesNotification: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
