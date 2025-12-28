package com.example.attendancewifi.data.models

data class StudentAttendance(
    val id: String,
    val name: String,
    val attendance: Map<String, Boolean>
)
