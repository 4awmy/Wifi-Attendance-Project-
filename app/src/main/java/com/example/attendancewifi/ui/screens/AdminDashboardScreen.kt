package com.example.attendancewifi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendancewifi.viewmodel.AttendanceViewModel

private val PrimaryBlue = Color(0xFF132B5B)
private val LightGrayBg = Color(0xFFF4F7FB)
private val CardBg = Color.White



@Composable
fun StyledTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = PrimaryBlue,
        indicator = { tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp)
                    .background(Color.White)
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = if (selectedTabIndex == index)
                            FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }
    }
}

@Composable
fun AdminDashboardScreen(
    viewModel: AttendanceViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Manage Courses", "Add Instructor", "Add Student")

    LaunchedEffect(Unit) {
        viewModel.loadAdminData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBg)
    ) {


            Text(
                text = "Admin Dashboard",
                color = PrimaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp)
            )


        Spacer(modifier = Modifier.height(16.dp))

        StyledTabRow(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )


        Spacer(modifier = Modifier.height(16.dp))

        //  CONTENT CARD
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardBg)
        ) {
            when (selectedTabIndex) {
                0 -> ManageCoursesTab(viewModel, uiState.courses, uiState.instructors)
                1 -> AddInstructorTab(viewModel)
                2 -> AddStudentTab(viewModel)
            }
        }
    }
}
@Composable
fun AddStudentTab(viewModel: AttendanceViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        //  TITLE
        Text(
            text = "Add New Student",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF132B5B)
        )

        Text(
            text = "Create a student account and assign an ID",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        //  Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        //  Student ID
        OutlinedTextField(
            value = studentId,
            onValueChange = { studentId = it },
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        //  Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        //  Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        //  ACTION BUTTON
        Button(
            onClick = {
                if (
                    name.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    studentId.isNotBlank()
                ) {
                    viewModel.createStudent(email, password, name, studentId)

                    // Clear fields
                    name = ""
                    email = ""
                    password = ""
                    studentId = ""
                }
            },
            enabled = name.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank() &&
                    studentId.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF132B5B),
                disabledContainerColor = Color(0xFF132B5B).copy(alpha = 0.4f),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Create Student",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ManageCoursesTab(
    viewModel: AttendanceViewModel,
    courses: List<com.example.attendancewifi.data.models.Course>,
    instructors: List<com.example.attendancewifi.data.models.User>
) {
    var newCourseName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 📘 SECTION TITLE
        Text(
            text = "Manage Courses",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )

        Text(
            text = "Create courses and assign instructors",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        //  ADD COURSE CARD
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = newCourseName,
                    onValueChange = { newCourseName = it },
                    label = { Text("Course Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (newCourseName.isNotBlank()) {
                            viewModel.addCourse(newCourseName)
                            newCourseName = ""
                        }
                    },
                    enabled = newCourseName.isNotBlank(),
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        disabledContainerColor = PrimaryBlue.copy(alpha = 0.4f),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Add",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        //  COURSE LIST HEADER
        Text(
            text = "Existing Courses",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = PrimaryBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Divider()

        Spacer(modifier = Modifier.height(12.dp))

        //  COURSE LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize() ,
            verticalArrangement = Arrangement.spacedBy(12.dp),

        ) {
            items(courses) { course ->
                CourseItem(
                    course = course,
                    instructors = instructors,
                    onAssign = { courseName, instructorId ->
                        viewModel.assignInstructor(courseName, instructorId)
                    }
                )
            }
        }
    }
}
@Composable
fun CourseItem(
    course: com.example.attendancewifi.data.models.Course,
    instructors: List<com.example.attendancewifi.data.models.User>,
    onAssign: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val currentInstructorName =
        instructors.find { it.uid == course.instructorId }?.name ?: "Unassigned"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            //  Course Name
            Text(
                text = course.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF132B5B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            //  Instructor Dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = currentInstructorName,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Instructor") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Instructor",
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
                    instructors.forEach { instructor ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = instructor.name,
                                    fontSize = 14.sp
                                )
                            },
                            onClick = {
                                onAssign(course.name, instructor.uid)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AddInstructorTab(viewModel: AttendanceViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        //  TITLE
        Text(
            text = "Add New Instructor",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )

        Text(
            text = "Create an instructor account",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        //  Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        //  Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        //  Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        //  ACTION BUTTON
        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                    viewModel.createInstructor(email, password, name)

                    // Clear fields
                    name = ""
                    email = ""
                    password = ""
                }
            },
            enabled = name.isNotBlank() &&
                    email.isNotBlank() &&
                    password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                disabledContainerColor = PrimaryBlue.copy(alpha = 0.4f),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Create Instructor",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

