package com.example.attendancewifi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CoursesScreen(onCourseClick: (String) -> Unit) {
    // 1. Hardcoded list of courses
    val courses = listOf(
        "Operating Systems",
        "System Programming",
        "Theory of Computation",
        "Database Systems"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Courses",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 2. A scrollable list (LazyColumn is like RecyclerView)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(courses) { courseName ->
                CourseItem(name = courseName, onClick = { onCourseClick(courseName) })
            }
        }
    }
}

// A generic button for each course
@Composable
fun CourseItem(name: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(60.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text = name, fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CoursesScreenPreview() {
    CoursesScreen(onCourseClick = {})
}