package com.mdq.social

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mdq.social.app.data.response.recent.DataItem

class PreferenceManager {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var mContext: Context? = null
    private val mPrivateMode = 0

    private val PREF_NAME = "PREF_MAKEOVER"
    private val PREF_PROFILR = "PREF_PROFILE"
    private val SWITCH_BIO = "SWITCH_BIO"
    private val PROFILE_NAME ="PROFILE_NAME"
    private val LISTOFDATA ="LISTOFDATA"
    private val FIREBASEUSERID ="FIREBASEUSERID"
    private val PREF_PROFILE_UID ="PREF_PROFILE_UID"

    private var mInstance: PreferenceManager? = null

    fun getInstance():PreferenceManager? {
        if (mInstance == null) mInstance = com.mdq.social.PreferenceManager()
        return mInstance
    }

    private fun PreferenceManager() {}

    fun initialize(context: Context?) {
        mContext = context
        sharedPreferences = mContext!!.getSharedPreferences(
            PREF_NAME,
            mPrivateMode
        )
        editor = sharedPreferences?.edit()
    }

    fun setPrefProfile(Profile: String?) {
        editor!!.putString(PREF_PROFILR, Profile)
        editor!!.commit()
    }

    fun getPrefProfile(): String? {
        return sharedPreferences!!.getString(PREF_PROFILR,
            null
        )
    }

     fun setSwitchBio(switches: Boolean?) {
         if (switches != null) {
             editor!!.putBoolean(SWITCH_BIO, switches)
         }

        editor!!.commit()
    }

    fun getSwitches(): Boolean? {
        return sharedPreferences!!.getBoolean(SWITCH_BIO,false)
    }

    fun setProfileName(name: String?) {
        if (name != null) {
            editor!!.putString(PROFILE_NAME, name)
        }
        editor!!.commit()
    }

    fun getProfileName(): String? {
        return sharedPreferences!!.getString(PROFILE_NAME,"Sanjai")
    }

    fun setFIREBASEID(name: String?) {
        if (name != null) {
            editor!!.putString(FIREBASEUSERID, name)
        }
        editor!!.commit()
    }

    fun getFIREBASEID(): String? {
        return sharedPreferences!!.getString(FIREBASEUSERID, "Sanjai")
    }

    fun setuid(name: String?) {
        if (name != null) {
            editor!!.putString(PREF_PROFILE_UID, name)
        }
        editor!!.commit()
    }

    fun getuid(): String? {
        return sharedPreferences!!.getString(PREF_PROFILE_UID, "Sanjai")
    }

    fun setList(List:List<DataItem?>){
        if(List!=null){
            var gson:Gson= Gson();
            var values:String=gson.toJson(List)
            editor!!.putString(LISTOFDATA, values)
            editor!!.apply()
        }
    }

    fun getList():List<DataItem>{

        var values:String?=sharedPreferences!!.getString(LISTOFDATA,"Sanjai")
        var gson:Gson=Gson()
        val type = object : TypeToken<ArrayList<DataItem?>?>() {}.type
        var list:List<DataItem> =gson.fromJson(values,type)
        return list

    }

}