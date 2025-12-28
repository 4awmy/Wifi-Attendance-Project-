package com.example.attendancewifi.data.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NetworkInfo(
    val ssid: String,
    val bssid: String,
    val timestamp: Long = System.currentTimeMillis()
)

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
