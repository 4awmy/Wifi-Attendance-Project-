package com.example.attendancewifi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    // Dropdown State (UNCHANGED)
    var expanded by remember { mutableStateOf(false) }
    val courses = listOf("CS101 - Intro to CS", "CS202 - Algorithms")
    var selectedCourse by remember { mutableStateOf(courses[0]) }

    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
    ) {

        /* 🔵 HEADER */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF132B5B))
                .padding(20.dp)
        ) {
            Text(
                text = "Instructor Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            /* 📘 COURSE SELECT */
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = "Select Course",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box {
                        OutlinedTextField(
                            value = selectedCourse,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
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
                                    text = { Text(course) },
                                    onClick = {
                                        selectedCourse = course
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* 📊 ATTENDANCE TITLE */
            Text(
                text = "Attendance Sheet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF132B5B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            /* 📋 ATTENDANCE TABLE */
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    /* HEADER ROW */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEAF0F6))
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Student",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(120.dp)
                        )

                        Row(
                            modifier = Modifier.horizontalScroll(horizontalScrollState)
                        ) {
                            uiState.attendanceDates.forEach { date ->
                                Text(
                                    text = date,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .padding(horizontal = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Divider()

                    /* STUDENT ROWS */
                    LazyColumn {
                        items(uiState.attendanceSheet) { student ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = student.name,
                                    modifier = Modifier.width(120.dp),
                                    maxLines = 1
                                )

                                Row(
                                    modifier = Modifier.horizontalScroll(horizontalScrollState)
                                ) {
                                    uiState.attendanceDates.forEach { date ->
                                        val isPresent =
                                            student.attendance[date] ?: false

                                        Box(
                                            modifier = Modifier.width(80.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Checkbox(
                                                checked = isPresent,
                                                onCheckedChange = {
                                                    viewModel.toggleAttendance(
                                                        student.id,
                                                        date
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Divider(color = Color(0xFFE0E0E0))
                        }
                    }
                }
            }
        }
    }
}
