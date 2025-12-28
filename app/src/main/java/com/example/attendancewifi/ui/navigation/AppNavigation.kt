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
import com.example.attendancewifi.ui.screens.AdminDashboard
import com.example.attendancewifi.ui.screens.InstructorDashboard

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // 1. LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        "admin" -> "admin_dashboard"
                        "instructor" -> "instructor_dashboard"
                        "student" -> "student_home"
                        else -> "student_home" // Default fallback
                    }
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 2. COURSES / STUDENT HOME
        composable("student_home") {
            CoursesScreen(
                onCourseClick = { courseName ->
                    navController.navigate("attendance/$courseName")
                }
            )
        }
        // Keep old route for backward compatibility if needed, or remove it.
        // For now, I will add an alias or just keep "courses" as well but the logic uses "student_home"
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

        // 4. ADMIN DASHBOARD
        composable("admin_dashboard") {
            AdminDashboard()
        }

        // 5. INSTRUCTOR DASHBOARD
        composable("instructor_dashboard") {
            InstructorDashboard()
        }
    }
}