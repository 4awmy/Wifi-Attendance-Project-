// kotlin
package com.example.attendancewifi.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

// Colors
private val PrimaryBlue = Color(0xFF132B5B)
private val SuccessGreen = Color(0xFF4CAF50)
private val ErrorRed = Color(0xFFE53935)
private val LightGrayBg = Color(0xFFF4F7FB)

@Composable
fun AttendanceScreen(
    courseId: String,
    courseName: String,
    viewModel: AttendanceViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            kotlinx.coroutines.delay(5000)
            viewModel.clearMessages()
        }
    }

    // local function to perform the wifi check + attendance marking
    fun doCheckAndMarkAttendance() {
        try {
            val wifiScanner = WifiScanner(context)
            val networkInfo = try {
                wifiScanner.getCurrentNetwork()
            } catch (e: Exception) {
                Toast.makeText(context, "Error scanning Wi‑Fi: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                null
            }

            val bssid = networkInfo?.bssid ?: ""
            if (bssid.isBlank() || bssid == "02:00:00:00:00:00") {
                Toast.makeText(context, "Enable Wi‑Fi and Location services", Toast.LENGTH_LONG).show()
                return
            }

            viewModel.markAttendance(
                name = "ziko",
                studentId = "231007795",
                courseName = courseName,
                DoctorName = "Reem",
                studentGroup = "Gp1",
                currentBssid = bssid
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Unexpected error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // automatically continue the operation after the user grants permission
            doCheckAndMarkAttendance()
        } else {
            Toast.makeText(
                context,
                "Location permission is required to verify Wi‑Fi.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBg)
    ) {
        // HEADER (Blue Background)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(PrimaryBlue),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = courseName,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = courseId,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // STATUS CARD
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        uiState.isLoading -> Color(0xFFE3F2FD)
                        uiState.successMessage != null -> Color(0xFFE8F5E9)
                        uiState.errorMessage != null -> Color(0xFFFDECEA)
                        else -> Color.White
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        uiState.isLoading -> {
                            Text(
                                text = "Checking Wi-Fi…",
                                color = PrimaryBlue,
                                fontSize = 15.sp
                            )
                        }

                        // kotlin
// Replace the success branch inside the status Card's Column with this:
                        uiState.successMessage != null -> {
                            Text(
                                text = uiState.successMessage !!,
                                color = SuccessGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }


                        uiState.errorMessage != null -> {
                            Text(
                                text = uiState.errorMessage!!,
                                color = ErrorRed,
                                fontSize = 14.sp
                            )
                        }

                        else -> {
                            Text(
                                text = "You are ready to take attendance",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    when (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        PackageManager.PERMISSION_GRANTED -> {
                            // permission already granted -> perform check
                            doCheckAndMarkAttendance()
                        }

                        else -> {
                            // request permission; on grant the permissionLauncher will call doCheckAndMarkAttendance()
                            permissionLauncher.launch(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }
                    }
                },

                enabled = !uiState.isLoading && uiState.successMessage == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    disabledContainerColor = PrimaryBlue,
                    contentColor = Color.White
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Take Attendance",
                        fontSize = 17.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        }
    }
}
