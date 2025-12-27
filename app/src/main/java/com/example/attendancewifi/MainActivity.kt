package com.example.attendancewifi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.attendancewifi.ui.theme.AttendanceWifiTheme
import com.example.attendancewifi.ui.navigation.AppNavigation // Import your nav file

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceWifiTheme {
                AppNavigation()
            }
        }
    }
}