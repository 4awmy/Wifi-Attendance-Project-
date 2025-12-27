package com.example.attendanceapp

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class AttendanceRepository {

    // Connect to Firestore
    private val db = FirebaseFirestore.getInstance()

    suspend fun markAttendance(studentId: String, name: String) {
        // Create a unique ID (User ID + Today's Date) to prevent duplicate submissions
        val today = android.text.format.DateFormat.format("yyyy-MM-dd", Date()).toString()
        val docId = "${studentId}-${today}"

        val data = hashMapOf(
            "name" to name,
            "id" to studentId,
            "date" to today,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        // Save to "attendance" collection
        db.collection("attendance").document(docId).set(data).await()
    }
}