package com.example.attendancewifi.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class AttendanceRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // <--- NEW: Login instead of Sign Up
    suspend fun loginUser(email: String, pass: String): String {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return result.user?.uid ?: throw Exception("Login failed")
    }



    // Attendance Logic
    suspend fun markAttendance(studentId: String, name: String, courseName: String) {
        val today = android.text.format.DateFormat.format("yyyy-MM-dd", Date()).toString()
        // Unique ID: StudentID + Date + Course (So you can attend multiple classes in one day)
        val docId = "${studentId}-${today}-${courseName}"

        val currentUserId = auth.currentUser?.uid ?: "anonymous"

        val data = hashMapOf(
            "name" to name,
            "studentId" to studentId,
            "course" to courseName,
            "auth_uid" to currentUserId,
            "date" to today,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("attendance").document(docId).set(data).await()
    }
}