package com.example.attendancewifi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.attendancewifi.ui.screens.LoginScreen
import com.example.attendancewifi.ui.screens.AttendanceScreen
import com.example.attendancewifi.ui.screens.CoursesScreen // Make sure to import this!

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // 1. LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("courses") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 2. COURSES (The Missing Link)
        composable("courses") {
            CoursesScreen(
                onCourseClick = { courseName ->
                    navController.navigate("attendance/$courseName")
                }
            )
        }

        // 3. ATTENDANCE (Now properly handles the String)
        composable(
            route = "attendance/{courseName}",
            arguments = listOf(navArgument("courseName") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseName = backStackEntry.arguments?.getString("courseName") ?: "Class"
            AttendanceScreen(courseName = courseName) // ✅ Now passing the required String
        }
    }
}