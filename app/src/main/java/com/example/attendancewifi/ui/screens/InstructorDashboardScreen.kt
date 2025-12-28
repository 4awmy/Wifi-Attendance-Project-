package com.example.attendancewifi.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendancewifi.viewmodel.AttendanceViewModel

@Composable
fun InstructorDashboardScreen(
    viewModel: AttendanceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Dropdown State
    var expanded by remember { mutableStateOf(false) }
    val courses = listOf("CS101 - Intro to CS", "CS202 - Algorithms")
    var selectedCourse by remember { mutableStateOf(courses[0]) }

    // Enroll State
    var studentIdToEnroll by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. TITLE ---
        Text(
            text = "Instructor Dashboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- 2. COURSE SELECTION (DROPDOWN) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = selectedCourse,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Course") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.clickable { expanded = true }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(text = course) },
                        onClick = {
                            selectedCourse = course
                            expanded = false
                            viewModel.loadAnalytics(course)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. ENROLL STUDENT SECTION ---
        Text(
            text = "Enroll Student",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = studentIdToEnroll,
                onValueChange = { studentIdToEnroll = it },
                label = { Text("Student ID") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (studentIdToEnroll.isNotBlank()) {
                        Toast.makeText(context, "Added $studentIdToEnroll", Toast.LENGTH_SHORT).show()
                        studentIdToEnroll = ""
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Enroll")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 4. ANALYTICS SECTION (BAR CHART) ---
        Text(
            text = "Attendance History",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.analyticsData.isEmpty()) {
            Text("No data available", color = Color.Gray)
        } else {
            SimpleBarChart(data = uiState.analyticsData)
        }
    }
}

@Composable
fun SimpleBarChart(data: Map<String, Int>) {
    val maxCount = data.values.maxOrNull() ?: 1

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        data.forEach { (date, count) ->
            // Calculate height fraction (e.g., 45/50)
            // Minimum height of 0.1f to show 0 values or small bars
            val heightFraction = (count.toFloat() / maxCount.toFloat()).coerceAtLeast(0.05f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                // The Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f) // Bar width relative to column
                        .fillMaxHeight(heightFraction)
                        .background(Color.Blue, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                )

                Spacer(modifier = Modifier.height(4.dp))

                // The Label (Date)
                // Simplify date for display if needed (e.g., remove year)
                val displayDate = date.substring(5) // Remove "YYYY-"
                Text(text = displayDate, fontSize = 12.sp)

                // The Value
                Text(text = count.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
