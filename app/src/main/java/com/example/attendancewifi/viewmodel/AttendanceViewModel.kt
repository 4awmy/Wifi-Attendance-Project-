package com.example.attendancewifi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancewifi.data.AttendanceRepository
import com.example.attendancewifi.data.AttendanceUiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val repository = AttendanceRepository()
    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()

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
                repository.loginUser(email, pass)
                // Success!
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
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

}