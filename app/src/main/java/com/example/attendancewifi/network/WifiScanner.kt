
package com.example.attendancewifi.network

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.ContextCompat
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

//    fun scanAvailableNetworks(): List<NetworkInfo> {
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//            != android.content.pm.PackageManager.PERMISSION_GRANTED) {
//            return emptyList()
//        }
//
//        wifiManager.startScan()
//        return wifiManager.scanResults.map { result ->
//            NetworkInfo(
//                ssid = result.SSID,
//                bssid = result.BSSID
//
//            )
//        }
//    }
}

