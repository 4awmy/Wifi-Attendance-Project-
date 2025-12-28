package com.example.attendancewifi.ui.screens

import android.Manifest // <-- Import Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendancewifi.network.WifiScanner
import com.example.attendancewifi.viewmodel.AttendanceViewModel


//@Composable
//fun AttendanceScreen(courseName: String) {
////    throw RuntimeException("I AM RUNNING ATTENDANCE SCREEN!");
//    var statusMessage by remember { mutableStateOf("Ready to mark attendance") }
//    var isButtonEnabled by remember { mutableStateOf(true) }
//    var statusColor by remember { mutableStateOf(Color.Gray) }
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        // TITLE
//        Text(text = courseName, fontSize = 28.sp)
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(text = "Mark Attendance", fontSize = 20.sp, color = Color.Gray)
//
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Text(
//            text = statusMessage,
//            fontSize = 18.sp,
//            color = statusColor,
//            modifier = Modifier.padding(8.dp)
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Button(
//            onClick = {
//                isButtonEnabled = false
//                statusMessage = "Checking Location..."
//                statusColor = Color.Blue
//
//                // Simulate a fake delay to show UI updating (Verification simulation)
//                // We will replace this later with the real WiFi check
//                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
//                    statusMessage = "✅ Success! Marked Present."
//                    statusColor = Color(0xFF4CAF50) // Green
//                    isButtonEnabled = true
//                }, 2000)
//            },
//            enabled = isButtonEnabled,
//            modifier = Modifier
//                .fillMaxWidth() // Button takes full width
//                .height(50.dp)
//        ) {
//            Text(text = "Check Attendance")
//        }
//    }
@Composable
fun AttendanceScreen(
    courseName: String,
    viewModel: AttendanceViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // --- PERMISSION HANDLING LOGIC ---
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permission was granted. Inform the user to try again.
                Toast.makeText(context, "Location permission granted. Please check attendance again.", Toast.LENGTH_SHORT).show()
            } else {
                // Permission was denied.
                Toast.makeText(context, "Location permission is required to verify Wi-Fi.", Toast.LENGTH_LONG).show()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // TITLE
        Text(
            text = courseName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mark Attendance",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // STATUS TEXT
        when {
            uiState.isLoading ->
                Text("Checking Wi-Fi...", color = Color.Blue)

            uiState.successMessage != null ->
                Text(uiState.successMessage!!, color = Color(0xFF4CAF50))

            uiState.errorMessage != null ->
                Text(uiState.errorMessage!!, color = Color.Red)

            else ->
                Text("Ready to mark attendance", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Check for location permission before scanning
                when (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    PackageManager.PERMISSION_GRANTED -> {
                        // Permission is already granted, proceed with attendance check
                        val wifiScanner = WifiScanner(context)
                        val networkInfo = wifiScanner.getCurrentNetwork()

                        // Add a check to prevent sending the placeholder BSSID
                        if (networkInfo.bssid == "02:00:00:00:00:00" || networkInfo.bssid.isBlank()) {
                            Toast.makeText(context, "Could not get Wi-Fi BSSID. Please ensure Location is enabled on your device.", Toast.LENGTH_LONG).show()
                            return@Button
                        }

                        viewModel.markAttendance(
                            name = "Nariman",
                            studentId = "231006695",
                            courseName = courseName, // Use the dynamic course name from the navigation argument
                            DoctorName = "Reem",
                            studentGroup = "Gp1",
                            currentBssid = networkInfo.bssid
                        )
                    }
                    else -> {
                        // Permission is not granted, launch the permission request dialog
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Check Attendance")
        }
    }

    // This block is removed because the ViewModel now handles showing success/error states
    // through the status text. Showing a Toast can be redundant. If you still want them,
    // you can uncomment this.
    /*
    uiState.successMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        // Consider resetting the message in the ViewModel after showing it
    }

    uiState.errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        // Consider resetting the message in the ViewModel after showing it
    }
    */
}
