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
import com.example.attendancewifi.ui.screens.AdminDashboardScreen
import com.example.attendancewifi.ui.screens.InstructorDashboardScreen

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
                onCourseClick = { course ->
                    navController.navigate("attendance/${course.id}/${course.name}")
                }
            )

        }
        // Keep old route for backward compatibility if needed, or remove it.
        // For now, I will add an alias or just keep "courses" as well but the logic uses "student_home"
        composable("courses") {
            CoursesScreen(
                onCourseClick = { course ->
                    navController.navigate("attendance/${course.id}/${course.name}")
                }
            )

        }

        // 3. ATTENDANCE (Now properly handles the String)
        composable(
            route = "attendance/{courseId}/{courseName}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("courseName") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val courseName = backStackEntry.arguments?.getString("courseName") ?: ""

            AttendanceScreen(
                courseId = courseId,
                courseName = courseName
            )
        }


        // 4. ADMIN DASHBOARD
        composable("admin_dashboard") {
            AdminDashboardScreen()
        }

        // 5. INSTRUCTOR DASHBOARD
        composable("instructor_dashboard") {
            InstructorDashboardScreen()
        }
    }
}