package com.mdq.social.app.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class AppPreference @Inject constructor(context: Context) {

    companion object {
        private val PREFERENCE_NAME = "DATABINDING_PREF"
        private val MOBILENU = "MOBILENU"
        private val USER_ID = "USER_ID"
        private val EMAIL = "EMAIL"
        private val NAME = "NAME"
        private val USERNAME = "USERNAME"
        private val PROFILE = "PROFILE"
        private val FIREBASEUSERID = "FIREBASEUSERID"
        private val USERGROUP = "USERGROUP"
        private val PRIVACY = "PRIVACY"
        private val LIKEANDCOMMENT = "LIKEANDCOMMENT"
        private val MESSAGE = "MESSAGE"
        private val POSTS = "POSTS"
        private val FOLLOWERREQUEST = "FOLLOWERREQUEST"
        private val FOLLOWERACCEPTANCE = "FOLLOWERACCEPTANCE"
        private val TYPE = "TYPE"
        private val LIVECHAT = "LIVECHAT"
        private val MUTELIST = "MUTELIST"
        private val ADMINBLOCK = "ADMINBLOCK"
        private val FIREBASETOKEN = "FIREBASETOKEN"
    }

    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    /*---------------------------------------------------------Clear Preference -----------------------------------------------------------*/

    fun clearAppPreference() {
        preferences.edit().clear().apply()
    }

    /*----------------------------------------------------------MobileNumber-------------------------------------------------------------------*/

    var MOBILENU: String
        set(value) = preferences.edit().putString(Companion.MOBILENU, value).apply()
        get() = preferences.getString(Companion.MOBILENU, "")!!

    /*----------------------------------------------------------Profile-------------------------------------------------------------------*/

    var PROFILE: String
        set(value) = preferences.edit().putString(Companion.PROFILE, value).apply()
        get() = preferences.getString(Companion.PROFILE, "")!!

    /*----------------------------------------------------------Email-------------------------------------------------------------------*/
    var EMAIL: String
        set(value) = preferences.edit().putString(Companion.EMAIL, value).apply()
        get() = preferences.getString(Companion.EMAIL, "")!!

    /*----------------------------------------------------------UserId-------------------------------------------------------------------*/
    var USERID: String
        set(value) = preferences.edit().putString(Companion.USER_ID, value).apply()
        get() = preferences.getString(Companion.USER_ID, "")!!

    /*----------------------------------------------------------Name-------------------------------------------------------------------*/
    var NAME: String
        set(value) = preferences.edit().putString(Companion.NAME, value).apply()
        get() = preferences.getString(Companion.NAME, "")!!


    /*----------------------------------------------------------Username-------------------------------------------------------------------*/
    var USERNAME: String
        set(value) = preferences.edit().putString(Companion.USERNAME, value).apply()
        get() = preferences.getString(Companion.USERNAME, "")!!


    /*----------------------------------------------------------LIVECHAT-------------------------------------------------------------------*/
    var LIVECHAT: String
        set(value) = preferences.edit().putString(Companion.LIVECHAT, value).apply()
        get() = preferences.getString(Companion.LIVECHAT, "")!!


    /*----------------------------------------------------------UserGroup-------------------------------------------------------------------*/

    var USERGROUP: String
        set(value) = preferences.edit().putString(Companion.USERGROUP, value).apply()
        get() = preferences.getString(Companion.USERGROUP, "")!!

    /*----------------------------------------------------------FIREBASEUSERID-------------------------------------------------------------------*/
    var FIREBASEUSERID: String
        set(value) = preferences.edit().putString(Companion.FIREBASEUSERID, value).apply()
        get() = preferences.getString(Companion.FIREBASEUSERID, "")!!

    /*----------------------------------------------------------PRIVACY-------------------------------------------------------------------*/
    var PRIVACY: String
        set(value) = preferences.edit().putString(Companion.PRIVACY, value).apply()
        get() = preferences.getString(Companion.PRIVACY, "0")!!

    /*----------------------------------------------------------TYPE-------------------------------------------------------------------*/
    var TYPE: String
        set(value) = preferences.edit().putString(Companion.TYPE, value).apply()
        get() = preferences.getString(Companion.TYPE, "")!!

    /*----------------------------------------------------------LIKEANDCOMMENT-------------------------------------------------------------------*/
    var LIKEANDCOMMENT: String
        set(value) = preferences.edit().putString(Companion.LIKEANDCOMMENT, value).apply()
        get() = preferences.getString(Companion.LIKEANDCOMMENT, "")!!

    /*----------------------------------------------------------MESSAGE-------------------------------------------------------------------*/
    var MESSAGE: String
        set(value) = preferences.edit().putString(Companion.MESSAGE, value).apply()
        get() = preferences.getString(Companion.MESSAGE, "")!!

    /*----------------------------------------------------------POSTS-------------------------------------------------------------------*/
    var POSTS: String
        set(value) = preferences.edit().putString(Companion.POSTS, value).apply()
        get() = preferences.getString(Companion.POSTS, "")!!

    /*----------------------------------------------------------FOLLOWERREQUEST-------------------------------------------------------------------*/
    var FOLLOWERREQUEST: String
        set(value) = preferences.edit().putString(Companion.FOLLOWERREQUEST, value).apply()
        get() = preferences.getString(Companion.FOLLOWERREQUEST, "")!!

    /*----------------------------------------------------------ADMINBLOCK-------------------------------------------------------------------*/
    var ADMINBLOCK: String
        set(value) = preferences.edit().putString(Companion.ADMINBLOCK, value).apply()
        get() = preferences.getString(Companion.ADMINBLOCK, "")!!

    /*----------------------------------------------------------FOLLOWERACCEPTANCE-------------------------------------------------------------------*/
    var FOLLOWERACCEPTANCE: String
        set(value) = preferences.edit().putString(Companion.FOLLOWERACCEPTANCE, value).apply()
        get() = preferences.getString(Companion.FOLLOWERACCEPTANCE, "")!!

    /*----------------------------------------------------------FIREBASETOKEN-------------------------------------------------------------------*/
    var FIREBASETOKEN: String
        set(value) = preferences.edit().putString(Companion.FIREBASETOKEN, value).apply()
        get() = preferences.getString(Companion.FIREBASETOKEN, "")!!

/*----------------------------------------------------------FOLLOWERACCEPTANCE-------------------------------------------------------------------*/

    fun setMuteList(List: ArrayList<String>) {
        if (List != null) {
            var gson: Gson = Gson()
            var values: String = gson.toJson(List)
            preferences.edit()!!.putString(MUTELIST, values)
            preferences.edit()!!.apply()
        }
    }

    fun getMuteList(): ArrayList<String> {
        var values: String? = preferences!!.getString(MUTELIST, "Sanjai")
        var gson: Gson = Gson()
        val type = object : TypeToken<ArrayList<String?>?>() {}.type
        var list: ArrayList<String>? = null
        var list1 = ArrayList<String>()
        list1.add("sam")
        list1.add("sam")
        list1.add("sam")
        list1.add("sam")
        if (!values.equals("Sanjai")) {
            list = gson.fromJson(values, type)
        }
        if (list != null) {
            return list!!
        } else {
            return list1
        }
    }
}