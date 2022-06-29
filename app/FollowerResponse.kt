data class FollowerResponse(

	@field:SerializedName("follower")
	val follower: List<FollowerItem?>? = null,

	@field:SerializedName("follower_pending")
	val followerPending: List<FollowerPendingItem?>? = null,

	@field:SerializedName("following")
	val following: List<FollowingItem?>? = null,

	@field:SerializedName("follower_reject")
	val followerReject: List<FollowerRejectItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class FollowerPendingItem(

	@field:SerializedName("follower_id")
	val followerId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("follow_status")
	val followStatus: Int? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class FollowerItem(

	@field:SerializedName("follower_id")
	val followerId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("follow_status")
	val followStatus: Int? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class FollowerRejectItem(

	@field:SerializedName("follower_id")
	val followerId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("follow_status")
	val followStatus: Int? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class FollowingItem(

	@field:SerializedName("follower_id")
	val followerId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("follow_status")
	val followStatus: Int? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

