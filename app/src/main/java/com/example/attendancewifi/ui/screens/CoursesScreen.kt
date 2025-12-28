package com.example.attendancewifi.ui.screens

import com.example.attendancewifi.data.models.Course
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* ---------- COLORS ---------- */

private val HeaderBlue = Color(0xFF132B5B)

/* ---------- SCREEN ---------- */

@Composable
fun CoursesScreen(onCourseClick: (Course) -> Unit) {

    val courses = listOf(
        Course("EBA3202", "Differential Equations"),
        Course("CIT3200", "Prof. Training in Mobile App"),
        Course("CCS3203", "Operating Systems"),
        Course("CCS3202", "Systems Programming"),
        Course("CAI3101", "Artificial Intelligence"),
        Course("CCS3402", "Database Systems")
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔵 Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(HeaderBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "My Courses",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(courses) { course ->
                CourseCard(course = course) {
                    onCourseClick(course)
                }
            }
        }
    }
}

/* ---------- CARD ---------- */

@Composable
fun CourseCard(course: Course, onClick: () -> Unit) {

    //  Random color theme (STABLE per course)
    val colors = remember(course.code) {
        listOf(
            // 🟢 Green
            listOf(
                Color(0xFFEAF4D3),
                Color(0xFF4A9F46),
                Color(0xFF4A9F46)
            ),
            // 🔵 Blue
            listOf(
                Color(0xFFE3ECFF),
                Color(0xFF4B6EDC),
                Color(0xFF4B6EDC)
            ),
            // 🌸 Pink
            listOf(
                Color(0xFFFFE1EC),
                Color(0xFFE75C8D),
                Color(0xFFE75C8D)
            )
        ).random()
    }

    val topColor = colors[0]
    val bottomColor = colors[1]
    val accentColor = colors[2]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(bottom = 12.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(bottomColor)
        ) {

            // 🔝 Top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(topColor),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = HeaderBlue,
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = course.code,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }

            // 🔽 Bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = course.name,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ➡️ Arrow
        FloatingActionButton(
            onClick = onClick,
            containerColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 16.dp)
                .size(42.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = accentColor
            )
        }
    }
}
