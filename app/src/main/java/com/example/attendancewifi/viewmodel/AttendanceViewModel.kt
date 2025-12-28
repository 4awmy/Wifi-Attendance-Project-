package com.example.attendancewifi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancewifi.data.AttendanceRepository
import com.example.attendancewifi.data.AttendanceUiState
import com.example.attendancewifi.data.models.StudentAttendance
import com.example.attendancewifi.network.WifiScanner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val repository = AttendanceRepository()
    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Load initial analytics for default course
        loadAnalytics("CS101 - Intro to CS")
        loadDummyAttendanceSheet()
    }

    fun loginUser(email: String, pass: String) {
        // Validation
        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter email and password") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Call the Repository Login
                val uid = repository.loginUser(email, pass)
                val role = repository.getUserRole(uid)
                // Success!
                _uiState.update { it.copy(isLoading = false, isSuccess = true, role = role) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Login Failed: ${e.message}") }
            }
        }
    }
    fun markAttendance(
        name: String,
        courseName: String,
        studentId: String,
        DoctorName :String,
        studentGroup: String,
        currentBssid: String
    ) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val result = repository.checkAndMarkAttendance(

                    name = name,
                    CourseName = courseName,
                    studentId = studentId,
                    DoctorName=DoctorName,
                    studentGroup = studentGroup,
                    currentBssid = currentBssid
                )

                _uiState.update {
                    it.copy(isLoading = false, successMessage = result)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun loadAnalytics(courseName: String) {
        val data = getDummyAnalytics(courseName)
        _uiState.update { it.copy(analyticsData = data) }
    }

    private fun getDummyAnalytics(courseName: String): Map<String, Int> {
        // Hardcoded data for testing
        return if (courseName.contains("CS101")) {
            mapOf(
                "2023-11-01" to 45,
                "2023-11-03" to 42,
                "2023-11-05" to 48
            )
        } else {
            // Just some different dummy data for variety
            mapOf(
                "2023-11-02" to 30,
                "2023-11-04" to 35,
                "2023-11-06" to 32
            )
        }
    }

    private fun loadDummyAttendanceSheet() {
        // Hardcoded Dates
        val dates = listOf("Mon 10/1", "Tue 10/2", "Wed 10/3", "Thu 10/4", "Fri 10/5")

        // Hardcoded Students
        val sheet = listOf(
            StudentAttendance("s1", "Alice Smith",
                mapOf("Mon 10/1" to true, "Tue 10/2" to true, "Wed 10/3" to true, "Thu 10/4" to true, "Fri 10/5" to true)),
            StudentAttendance("s2", "Bob Jones",
                mapOf("Mon 10/1" to true, "Tue 10/2" to false, "Wed 10/3" to true, "Thu 10/4" to false, "Fri 10/5" to true)),
            StudentAttendance("s3", "Charlie Brown",
                mapOf("Mon 10/1" to false, "Tue 10/2" to false, "Wed 10/3" to false, "Thu 10/4" to true, "Fri 10/5" to true)),
            StudentAttendance("s4", "David White",
                mapOf("Mon 10/1" to true, "Tue 10/2" to true, "Wed 10/3" to true, "Thu 10/4" to true, "Fri 10/5" to false)),
            StudentAttendance("s5", "Eve Black",
                mapOf("Mon 10/1" to true, "Tue 10/2" to true, "Wed 10/3" to false, "Thu 10/4" to true, "Fri 10/5" to true))
        )

        _uiState.update { it.copy(attendanceDates = dates, attendanceSheet = sheet) }
    }

    fun toggleAttendance(studentId: String, date: String) {
        val currentSheet = _uiState.value.attendanceSheet.toMutableList()
        val studentIndex = currentSheet.indexOfFirst { it.id == studentId }

        if (studentIndex != -1) {
            val student = currentSheet[studentIndex]
            val newAttendanceMap = student.attendance.toMutableMap()

            // Toggle
            val currentValue = newAttendanceMap[date] ?: false
            newAttendanceMap[date] = !currentValue

            // Update List
            currentSheet[studentIndex] = student.copy(attendance = newAttendanceMap)

            _uiState.update { it.copy(attendanceSheet = currentSheet) }
        }
    }

}