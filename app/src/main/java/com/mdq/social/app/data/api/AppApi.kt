package com.mdq.social.app.data.api

import com.mdq.social.app.data.response.CommentResponse.CommentResponse
import com.mdq.social.app.data.response.CommentResponseForblock.CommentresponseforBlock
import com.mdq.social.app.data.response.NotificationSetting.NotificationSettingResponse
import com.mdq.social.app.data.response.RateCard.RateCardResponse
import com.mdq.social.app.data.response.Rating.Ratings
import com.mdq.social.app.data.response.ShopItem
import com.mdq.social.app.data.response.UserProfileDetailResponse.UserProfileDetailResponse
import com.mdq.social.app.data.response.addalbumresponse.AddAlbumResponse
import com.mdq.social.app.data.response.addcomment.AddCommentResponse
import com.mdq.social.app.data.response.addlikecomments.AddLikeCommentsResponse
import com.mdq.social.app.data.response.bookmarklist.BookMarkListResponse
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.chatBlockStatus.ChatBlockedStatus
import com.mdq.social.app.data.response.commentblocklist.commentBlockList
import com.mdq.social.app.data.response.connectionrequest.ConnectionRequest
import com.mdq.social.app.data.response.followresponse.FollowResponse
import com.mdq.social.app.data.response.forgotpassword.ForgotPasswordResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.NewPostsearchDeailResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.UserSearchDetailResponse
import com.mdq.social.app.data.response.livechat.ChatCount
import com.mdq.social.app.data.response.livechat.LiveChatResponse
import com.mdq.social.app.data.response.livechatprofile.LiveChatProfileResponse
import com.mdq.social.app.data.response.login.LoginResponse
import com.mdq.social.app.data.response.notification.NotificationListResponse
import com.mdq.social.app.data.response.privacy.PrivacyDetail
import com.mdq.social.app.data.response.profileupdate.ProfileUpdateResponse
import com.mdq.social.app.data.response.recent.RecentResponse
import com.mdq.social.app.data.response.reviewresponse.reviewResponse
import com.mdq.social.app.data.response.signup.SignupResponse
import com.mdq.social.app.data.response.times.TimesResponse
import com.mdq.social.app.data.response.updatedLikeUnlike.updatedLikeUnlike
import com.mdq.social.app.data.response.user_profile.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable
import kotlin.collections.HashMap

interface AppApi {
    @FormUrlEncoded
    @POST(ApiConstants.API_LOGIN)
    fun getLogin(@FieldMap hashMap: HashMap<String, String>): Observable<LoginResponse>

    @Multipart
    @POST(ApiConstants.API_SIGNUP)
    fun getSignUp(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part files: MultipartBody.Part
    ): Observable<SignupResponse>

    @Multipart
    @POST(ApiConstants.API_SIGNUP)
    fun getSignUpBusiness(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part files: MultipartBody.Part
    ): Observable<SignupResponse>

    @Multipart
    @POST(ApiConstants.API_UPDATE)
    fun getSignUpUpdate(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part files: MultipartBody.Part
    ): Observable<SignupResponse>

    @Multipart
    @POST(ApiConstants.API_UPDATE)
    fun getSignUpNormalUpdate(
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Observable<SignupResponse>

    @Multipart
    @POST(ApiConstants.API_SIGNUP)
    fun getSignUpUser(@PartMap hashMap: HashMap<String, RequestBody>): Observable<SignupResponse>

    @Multipart
    @POST(ApiConstants.FIREBASEUID)
    fun setFireBaseUID(@PartMap hashMap: HashMap<String, RequestBody>): Observable<SignupResponse>

    /*For GET Category List*/
    @GET(ApiConstants.API_CATEGORY)
    fun getCategory(): Observable<CategoryResponse>

    @GET(ApiConstants.API_GETTIMES)
    fun getTimes(): Observable<TimesResponse>

    /*Get All Post*/
    @GET(ApiConstants.API_GETUSERALBUMDETAILS)
    fun getRecent(@Header("userid") userid: String): Observable<RecentResponse>

    /*Get All Post*/
    @GET(ApiConstants.API_GETUSERALBUMDETAILS)
    fun getNotification(@Header("userid") userid: String): Observable<RecentResponse>

    /*Get Notification setting*/
    @GET(ApiConstants.NOTIFICATION_SWITCH)
    fun getNotificationSetting(@Header("user_id") userid: String): Observable<NotificationSettingResponse>

    /*Post Notification setting*/
    @Multipart
    @POST(ApiConstants.NOTIFICATION_SWITCH)
    fun PostNotificationSetting(@PartMap hashMap: HashMap<String, RequestBody>): Observable<SignupResponse>

    /*Delete Post*/
    @FormUrlEncoded
    @POST(ApiConstants.API_DELETE_POST)
    fun DeletePost(
        @Field("post_id") postid: String,
        @Field("user_id") userid: String
    ): Observable<SignupResponse>

    /*Get Comments*/
    @GET(ApiConstants.API_COMMENTDETAILS)
    fun getCommentDetails(@Header("postid") PostId: String): Observable<CommentResponse>

    /*Get Search Post*/
    @GET(ApiConstants.API_POST_SEARCH_DETAIL)
    fun getSearchPostDetails(@HeaderMap hashmap: HashMap<String, String>): Observable<NewPostsearchDeailResponse>

    /*Get Filter Search Post*/
    @GET(ApiConstants.API_POST_FILTER_DETAIL)
    fun getSearchFilterPostDetails(@HeaderMap hashmap: HashMap<String, String>): Observable<NewPostsearchDeailResponse>

    /*Get Filter User Post*/
    @GET(ApiConstants.API_POST_FILTER_USER_ETAIL)
    fun getSearchFilterUserDetails(@HeaderMap hashmap: HashMap<String, String>): Observable<UserSearchDetailResponse>

    /*Get Search USer*/
    @GET(ApiConstants.API_USER_SEARCH_DETAIL)
    fun getSearchUserDetails(@HeaderMap hashmap: HashMap<String, String>): Observable<UserSearchDetailResponse>

    @FormUrlEncoded
    @POST(ApiConstants.API_ADD_LIKE_COMMENTS)
    fun getAddLike(@FieldMap hashmap: HashMap<String, String>): Observable<AddLikeCommentsResponse>

    @FormUrlEncoded
    @POST(ApiConstants.UNLIKE)
    fun getAddUnLike(@FieldMap hashmap: HashMap<String, String>): Observable<AddLikeCommentsResponse>

    /*Hide Post*/
    @FormUrlEncoded
    @POST(ApiConstants.HIDE_POST)
    fun hidePost(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Get User Post*/
    @GET(ApiConstants.API_USER_PROFILE)
    fun getUserProfile(
        @Header("userid") id: String,
        @Header("userpost") userpost: String
    ): Observable<UserProfileResponse>

    @GET(ApiConstants.API_USER_PROFILE)
    fun getUserProfileForPostScreen(
        @HeaderMap hashMap: HashMap<String, String>
    ): Observable<UserProfileResponse>

    /*Get User Profile detail*/
    @GET(ApiConstants.API_USER_PROFILE_DETAILS)
    fun getUserProfileDetails(@Header("id") id: String): Observable<UserProfileDetailResponse>

    /*Get Notification list*/
    @GET(ApiConstants.API_GET_NOTIFICATION)
    fun getNotificationList(@Header("to_id") id: String): Observable<NotificationListResponse>

    /*Get User Profile detail*/
    @GET(ApiConstants.API_SHOP_TIMING)
    fun getShopTiming(@Header("userid") id: String): Observable<ShopItem>

    /*Get RateCard*/
    @GET(ApiConstants.API_RATING)
    fun getRatings(@Header("user_id") id: String): Observable<Ratings>

    /*Get RateCard*/
    @GET(ApiConstants.API_RATE_CARD)
    fun getRateCard(@Header("user_id") id: String): Observable<RateCardResponse>

    /*Delete RateCard*/
    @FormUrlEncoded
    @POST(ApiConstants.API_DELETE_RATE_CARD)
    fun DeleteRateCard(@Field("id") id: String): Observable<SignupResponse>

    /*Post RateCard*/
    @Multipart
    @POST(ApiConstants.API_RATE_CARD)
    fun PostRateCard(
        @Part("user_id") user_id: RequestBody,
        @Part image: MultipartBody.Part
    ): Observable<SignupResponse>

    /*For post*/
    @Multipart
    @POST(ApiConstants.API_ADD_ALBUM)
    fun updatePost(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part image: List<MultipartBody.Part>
    ): Observable<AddAlbumResponse>

    /*Add Comments*/
    @FormUrlEncoded
    @POST(ApiConstants.API_ADDCOMMENTS)
    fun getAddComment(@FieldMap hashmap: HashMap<String, String>): Observable<AddCommentResponse>

    /*Save Bookmark*/
    @FormUrlEncoded
    @POST(ApiConstants.API_BOOKMARKLIST)
    fun SaveBook(
        @Field("user_id") userid: String,
        @Field("post_id") post_id: String
    ): Observable<SignupResponse>

    @FormUrlEncoded
    @POST(ApiConstants.API_RESET_PASSWORD)
    fun getForgot(@FieldMap hashmap: HashMap<String, String>): Observable<ForgotPasswordResponse>

    @GET(ApiConstants.API_BOOKMARKLIST)
    fun getBookMarklist(@HeaderMap hashmap: HashMap<String, String>): Observable<BookMarkListResponse>

    @FormUrlEncoded
    @POST(ApiConstants.API_FOLLOW_REVIEWS)
    fun getFollowReviews(@FieldMap hashmap: HashMap<String, String>): Observable<UserProfileResponse>

    @FormUrlEncoded
    @POST(ApiConstants.UNFOLLOW)
    fun getUnFollowReviews(@FieldMap hashmap: HashMap<String, String>): Observable<UserProfileResponse>

    /*User Block*/
    @FormUrlEncoded
    @POST(ApiConstants.BLOCK)
    fun postBlock(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Chat Block*/
    @FormUrlEncoded
    @POST(ApiConstants.CHAT_BLOCK)
    fun ChatBlock(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Chat UnBlock*/
    @FormUrlEncoded
    @POST(ApiConstants.CHAT_UNBLOCK)
    fun ChatUnBlock(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Chat UnBlock*/
    @GET(ApiConstants.GET_BLOCK_STATUS)
    fun ChatBlockStatus(@Header ("chatuser_id") chatuser_id:String): Observable<ChatBlockedStatus>

    /*Follower list*/
    @GET(ApiConstants.API_FOLLOW_LIST)
    fun getFollowFollowingList(@HeaderMap hashmap: HashMap<String, String>): Observable<FollowResponse>

    /*Following list*/
    @GET(ApiConstants.API_FOLLOWING_LIST)
    fun getFollowingList(@HeaderMap hashmap: HashMap<String, String>): Observable<FollowResponse>

    /*Connection Request List*/
    @GET(ApiConstants.API_CONNECTION_REQUEST_LIST)
    fun getConnectionRequestList(@HeaderMap hashmap: HashMap<String, String>): Observable<ConnectionRequest>

    /*Connection Request List*/
    @GET(ApiConstants.API_CONNECTION_IGNORE_LIST)
    fun getConnectionignoredList(@HeaderMap hashmap: HashMap<String, String>): Observable<ConnectionRequest>

    /*Connection accepts*/
    @FormUrlEncoded
    @POST(ApiConstants.API_FOLLOWACCEPT)
    fun getFollowAccept(
        @Field("user_id") userID: String,
        @Field("follower_id") followerID: String
    ): Observable<SignupResponse>

    /*Connection ignored*/
    @FormUrlEncoded
    @POST(ApiConstants.API_IGNORED)
    fun postIgnored(
        @Field("user_id") userID: String,
        @Field("follower_id") followerID: String
    ): Observable<SignupResponse>

    @GET(ApiConstants.API_ADD_LIKE_COMMENTS)
    fun getLikeList(@Header("postid") postid: String): Observable<updatedLikeUnlike>

    /*GET BLOCKED LIST*/
    @GET(ApiConstants.API_BLOCKED_LIST)
    fun getBlockList(@HeaderMap hashmap: HashMap<String, String>): Observable<ConnectionRequest>

    /*UNBLOCK*/
    @FormUrlEncoded
    @POST(ApiConstants.API_UNBLOCk)
    fun PostUnBlock(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Comment Block*/
    @FormUrlEncoded
    @POST(ApiConstants.API_COMMENT_BLOCK)
    fun CommentBlock(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Comment Block*/
    @FormUrlEncoded
    @POST(ApiConstants.API_COMMENT_UNBLOCK)
    fun CommentUNBlock(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Comment Block List*/
    @GET(ApiConstants.API_GET_COMMENT_BLOCK)
    fun getCommentBlockList(@HeaderMap hashmap: HashMap<String, String>): Observable<commentBlockList>

    /*Comment Block List*/
    @GET(ApiConstants.API_GET_COMMENT_BLOCK_BY_USER)
    fun getCommentBlockListForComment(@HeaderMap hashmap: HashMap<String, String>): Observable<CommentresponseforBlock>

    @Multipart
    @POST(ApiConstants.API_PROFILEUPDATE)
    fun profileUpdate(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part image: List<MultipartBody.Part?>
    ): Observable<ProfileUpdateResponse>


    @FormUrlEncoded
    @POST(ApiConstants.API_USERPRIVACY)
    fun privacyUpdate(
        @FieldMap hashMap: HashMap<String, String>,
    ): Observable<SignupResponse>

    @FormUrlEncoded
    @POST(ApiConstants.API_DEACTIVATE)
    fun deactivate(
        @FieldMap hashMap: HashMap<String, String>,
    ): Observable<SignupResponse>

    /*GET PRIVACY*/
    @GET(ApiConstants.API_USERPRIVACY_GET)
    fun privacyGet(
        @HeaderMap hashMap: HashMap<String, String>,
    ): Observable<PrivacyDetail>

    /*GET CHAT_BLOCK_STATUS*/
    @GET(ApiConstants.API_CHATSTATUS_GET)
    fun GetChatBlockStatus(
        @HeaderMap hashMap: HashMap<String, String>,
    ): Observable<ChatBlockedStatus>

    @FormUrlEncoded
    @POST(ApiConstants.API_CONTACTDEATAIL)
    fun HelpAndSupport(
        @FieldMap hashMap: HashMap<String, String>,
    ): Observable<SignupResponse>

    /*Review List*/
    @GET(ApiConstants.API_GET_REVIEW_LIST)
    fun getReviewList(@HeaderMap hashmap: HashMap<String, String>): Observable<reviewResponse>

    /*Add Review*/
    @FormUrlEncoded
    @POST(ApiConstants.API_GET_REVIEW_LIST)
    fun PostReview(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Add Rating*/
    @FormUrlEncoded
    @POST(ApiConstants.API_POST_RATING)
    fun PostRating(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*insert notification*/
    @Multipart
    @POST(ApiConstants.API_INSERT_NOTIFICATION)
    fun insertNotification(@PartMap hashmap: HashMap<String, RequestBody>): Observable<SignupResponse>

    /*insert notification*/
    @FormUrlEncoded
    @POST(ApiConstants.API_READ_NOTIFICATION)
    fun readNotificationCall(@Field("id") id: String): Observable<SignupResponse>

    /*insert notification*/
    @FormUrlEncoded
    @POST(ApiConstants.API_INSERT_CHART)
    fun insertChat(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

    /*Chat list*/
    @GET(ApiConstants.API_CHART_List)
    fun getLiveChatList(@HeaderMap hashmap: HashMap<String, String>): Observable<LiveChatResponse>

    /*latest Chat list*/
    @GET(ApiConstants.LATEST_CHAT_LIST)
    fun getChatList(@Header("from_id") from_id: String): Observable<LiveChatProfileResponse>

    /*Chat count*/
    @GET(ApiConstants.LATEST_CHAT_COUNT)
    fun getChatCount(@Header("to_id") to_id: String): Observable<ChatCount>

    /*Chat Seen Call*/
    @FormUrlEncoded
    @POST(ApiConstants.CHAT_SEEN_CALL)
    fun ChatSeenCall(@FieldMap hashmap: HashMap<String, String>): Observable<SignupResponse>

}