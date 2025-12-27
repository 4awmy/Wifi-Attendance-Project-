package com.example.attendanceapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val repository = AttendanceRepository()

    // StateFlow holds the current status (Loading? Success? Error?)
    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState = _uiState.asStateFlow()

    fun submitAttendance(isValidWifi: Boolean, studentId: String, name: String) {

        // 1. Check Wi-Fi (Member C's logic will plug in here)
        if (!isValidWifi) {
            _uiState.update { it.copy(errorMessage = "Invalid Wi-Fi! Connect to University Network.") }
            return
        }

        // 2. Show Loading
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        // 3. Send to Firebase
        viewModelScope.launch {
            try {
                repository.markAttendance(studentId, name)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Unknown Error") }
            }
        }
    }

    fun resetState() {
        _uiState.update { AttendanceUiState() }
    }
}