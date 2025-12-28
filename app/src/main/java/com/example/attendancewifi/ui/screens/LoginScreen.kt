package com.example.attendancewifi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.attendancewifi.viewmodel.AttendanceViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    viewModel: AttendanceViewModel = viewModel() // Inject ViewModel
) {
    // State variables for the Text Boxes
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Listen to the Database/Auth state
    val uiState by viewModel.uiState.collectAsState()

    // 1. EFFECT: If login is successful, navigate automatically
    LaunchedEffect(uiState.isSuccess, uiState.role) {
        if (uiState.isSuccess && uiState.role != null) {
            onLoginSuccess(uiState.role!!)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome Student", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(32.dp))

        // EMAIL INPUT
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // PASSWORD INPUT
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hides the password
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ERROR MESSAGE (If any)
        if (uiState.errorMessage != null) {
            Text(text = uiState.errorMessage!!, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // LOGIN BUTTON
        Button(
            onClick = { viewModel.loginUser(email, password) },
            enabled = !uiState.isLoading, // Disable button while loading
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Login")
            }
        }
    }
}