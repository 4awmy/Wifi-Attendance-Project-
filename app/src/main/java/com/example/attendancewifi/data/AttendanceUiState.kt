package com.example.attendancewifi.data

data class AttendanceUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isSignedUp: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)