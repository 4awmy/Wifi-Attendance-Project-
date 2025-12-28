package com.example.attendancewifi.data.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "", // "admin", "instructor", "student"
    val studentId: String? = null // Crucial for Students
)
