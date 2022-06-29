package com.mdq.social.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager


object NetworkUtils {

    fun turnOffGps(context: Context) {
        val intent = Intent("android.location.GPS_ENABLED_CHANGE")
        intent.putExtra("enabled", false)
        context.sendBroadcast(intent)
    }

    fun turnOnGps(context: Context) {
        val intent = Intent("android.location.GPS_ENABLED_CHANGE")
        intent.putExtra("enabled", true)
        context.sendBroadcast(intent)
    }

    /**
     * Check the Internet connection available status
     *
     * @param context - Context environment passed by this parameter
     * @return boolean true if the Internet Connection is Available and false otherwise
     */
    fun isConnected(context: Context): Boolean {
        //Connectivity manager instance
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Fetch Active internet connection from network info
        val netInfo = manager.activeNetworkInfo
        // return the network connection is active or not.
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    @JvmStatic
    fun isLocationEnable(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        return gps_enabled
    }

}
