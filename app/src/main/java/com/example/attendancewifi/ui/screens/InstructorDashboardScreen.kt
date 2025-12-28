package com.example.attendancewifi.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    // Dropdown State
    var expanded by remember { mutableStateOf(false) }
    val courses = listOf("CS101 - Intro to CS", "CS202 - Algorithms")
    var selectedCourse by remember { mutableStateOf(courses[0]) }

    // Scroll State for Synchronization
    val horizontalScrollState = rememberScrollState()

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
                        modifier = Modifier.clickable(onClick = { expanded = true })
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
                            // In a real app, we'd reload sheet based on course here
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. ATTENDANCE SHEET ---
        Text(
            text = "Attendance Sheet",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // HEADER ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fixed "Student" Header
            Text(
                text = "Student",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(100.dp)
            )

            // Scrollable Dates Header
            Row(
                modifier = Modifier.horizontalScroll(horizontalScrollState)
            ) {
                uiState.attendanceDates.forEach { date ->
                    Text(
                        text = date,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(80.dp).padding(horizontal = 4.dp),
                        maxLines = 1
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // STUDENT ROWS
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.attendanceSheet) { student ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fixed Name
                    Text(
                        text = student.name,
                        modifier = Modifier.width(100.dp),
                        maxLines = 1
                    )

                    // Scrollable Checkboxes
                    Row(
                        modifier = Modifier.horizontalScroll(horizontalScrollState)
                    ) {
                        uiState.attendanceDates.forEach { date ->
                            val isPresent = student.attendance[date] ?: false

                            Box(
                                modifier = Modifier.width(80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Checkbox(
                                    checked = isPresent,
                                    onCheckedChange = {
                                        viewModel.toggleAttendance(student.id, date)
                                    }
                                )
                            }
                        }
                    }
                }
                Divider()
            }
        }
    }
}
