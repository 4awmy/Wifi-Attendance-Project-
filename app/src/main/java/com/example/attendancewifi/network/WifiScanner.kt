
package com.example.attendancewifi.network


import android.annotation.SuppressLint
import android.content.Context

import android.net.wifi.WifiManager

import com.example.attendancewifi.data.models.NetworkInfo

class WifiScanner(private val context: Context) {
    @SuppressLint("ServiceCast")
    private val wifiManager = context.applicationContext
        .getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun getCurrentNetwork(): NetworkInfo {

        val wifiInfo = wifiManager.connectionInfo
        return NetworkInfo(
            ssid = wifiInfo.ssid?.replace("\"", "") ?: "Unknown",
            bssid = wifiInfo.bssid ?: "Unknown"
        )
    }


}

