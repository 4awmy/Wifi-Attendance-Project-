package com.example.attendancewifi.data
import com.example.attendancewifi.data.models.Course
import com.example.attendancewifi.data.models.StudentAttendance
import com.example.attendancewifi.data.models.User

data class AttendanceUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isSignedUp: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val role: String? = null,
    val analyticsData: Map<String, Int> = emptyMap(),
    val attendanceSheet: List<StudentAttendance> = emptyList(),
    val attendanceDates: List<String> = emptyList(),
    val instructors: List<User> = emptyList(),
    val courses: List<Course> = emptyList()
)