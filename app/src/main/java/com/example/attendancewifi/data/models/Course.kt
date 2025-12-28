package com.example.attendancewifi.data.models

data class Course(
    val id: String = "",
    val name: String = "",
    val instructorId: String = "",
    val enrolledStudentIds: List<String> = emptyList()
)
