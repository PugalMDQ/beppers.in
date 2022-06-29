package com.mdq.social.app.data.api

object ApiConstants {

    const val BASE_URL = "https://mdqualityapps.in/"

    /*Login*/
    const val API_LOGIN = "users/login/"

    /*Signup*/
    const val API_SIGNUP = "users"

    /*Update Firebase UID*/
    const val FIREBASEUID = "updatefirebaseuid"

    /*Update Profile*/
    const val API_UPDATE = "users/update"

    /*Category*/
    const val API_CATEGORY = "categories"

    /*Times*/
    const val API_GETTIMES = "gettimes"

    /*User Times*/
    const val API_USERTIMES = "getusertimes"

    /*User Album*/
    const val API_GETUSERALBUMDETAILS = "post"

    /*Delete Post*/
    const val API_DELETE_POST = "post/deletepost"

    /*User commentdetails*/
    const val API_COMMENTDETAILS = "comments"

    /*Post Search detail*/
    const val API_POST_SEARCH_DETAIL = "post/search"

    /*Post Filter Detail*/
    const val API_POST_FILTER_DETAIL = "post/filter"

    /*Post Filter Detail*/
    const val API_POST_FILTER_USER_ETAIL = "users/filter"

    /*User Search detail*/
    const val API_USER_SEARCH_DETAIL = "users/search"

    /*RATING*/
    const val API_RATING = "rating"

    /*add like and comments*/
    const val API_ADD_LIKE_COMMENTS = "like"

    /*follow accepted*/
    const val API_FOLLOWACCEPT = "followAccept"

    /*follow accepted*/
    const val API_IGNORED = "users/followIgnore"

    /*Profile*/
    const val API_USER_PROFILE = "post"

    /*Profile*/
    const val API_USER_PROFILE_DETAILS = "users"

    /*ShopTiming*/
    const val API_SHOP_TIMING = "shoptimings"

    /*RateCard*/
    const val API_RATE_CARD = "ratecards"

    /*Delete RateCard*/
    const val API_DELETE_RATE_CARD = "deleteratecard"

    /*AddAlbum*/
    const val API_ADD_ALBUM = "post"

    /*Add Comments*/
    const val API_ADDCOMMENTS = "comments"

    /*Terms Condtions*/
    const val API_TERMS = "termsAndCondtions"

    /*Bookmark*/
    const val API_BOOKMARKLIST = "users/bookmarks"

    /*Follow Reviews*/
    const val API_FOLLOW_REVIEWS = "follow"

    /*UnFollow*/
    const val UNFOLLOW = "unfollow"

    /*UnFollow*/
    const val UNLIKE = "unlike"

    /*HIDE_POST*/
    const val HIDE_POST = "post/hide"

    /*User BLOCK*/
    const val BLOCK = "users/block"

    /*chat BLOCK*/
    const val CHAT_BLOCK = "chatblock"

    /*chat UnBLOCK*/
    const val CHAT_UNBLOCK = "chatunblock"

    /*get BLOCK status*/
    const val GET_BLOCK_STATUS = "getchatblock"

    /*Follow List*/
    const val API_FOLLOW_LIST = "users"

    /*Following List*/
    const val API_FOLLOWING_LIST = "users/fetchFollowing"

    /*Connection Request List*/
    const val API_CONNECTION_REQUEST_LIST = "users/followRequest"

    /*Connection ignore List*/
    const val API_CONNECTION_IGNORE_LIST = "users/getIgnoreList"

    /*GET BLOCKED LIST*/
    const val API_BLOCKED_LIST = "users/getBlockList"

    /*UNBLOCK*/
    const val API_UNBLOCk = "users/unblock"

    /*Comment Block*/
    const val API_COMMENT_BLOCK = "users/blockComments"

    /*Comment UnBlock*/
    const val API_COMMENT_UNBLOCK = "users/unblockComments"

    /*Get Comment Blocked List*/
    const val API_GET_COMMENT_BLOCK = "users/blockComments"

    /*Get Comment Blocked List for comment screen*/
    const val API_GET_COMMENT_BLOCK_BY_USER = "users/blockedCommentsbyuser"

    /* Profile Update*/
    const val API_PROFILEUPDATE = "users/update"

    /* Privacy Update*/
    const val API_USERPRIVACY = "privacy"

    /*Deactivate account*/
    const val API_DEACTIVATE = "deactivate"

    /* Privacy GET*/
    const val API_USERPRIVACY_GET = "post/privacy"


    /* Chatstatus get in profile*/
    const val API_CHATSTATUS_GET = "chatblockprofile"

    /*GET REVIEW LIST*/
    const val API_GET_REVIEW_LIST = "users/reviews"

    /*POST RATING*/
    const val API_POST_RATING = "rating"

    /*Help and support*/
    const val API_CONTACTDEATAIL = "users/contactdetails"

    /*Reset Password*/
    const val API_RESET_PASSWORD = "users/changePassword"

    /*INSERT_NOTIFICATION*/
    const val API_INSERT_NOTIFICATION = "insertnotification"

    /*INSERT_NOTIFICATION*/
    const val API_READ_NOTIFICATION = "notifyread"

    /*GET_NOTIFICATION*/
    const val API_GET_NOTIFICATION = "getnotifyuser"

    /*INSERT_CHAT*/
    const val API_INSERT_CHART = "insertchat"

    /*CHAT_SEEN_CALL*/
    const val CHAT_SEEN_CALL = "chatseen"

    /*CHAT-LIST*/
    const val API_CHART_List = "getchatdetails"

    /*LATEST_CHAT_LIST*/
    const val LATEST_CHAT_LIST = "getlatestmesg"

    /*LATEST_CHAT_LIST*/
    const val LATEST_CHAT_COUNT = "chatcount"

    /*NOTIFICATION_SWITCH*/
    const val NOTIFICATION_SWITCH = "notifyswitch"

}