package com.example.attendancewifi.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.text.get

class AttendanceRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // <--- NEW: Login instead of Sign Up
    suspend fun loginUser(email: String, pass: String): String {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return result.user?.uid ?: throw Exception("Login failed")
    }

    suspend fun checkAndMarkAttendance(
        name: String,
        studentId: String,
        CourseName: String,
        DoctorName :String,
    studentGroup: String,
        currentBssid: String
    ): String {

        val today = android.text.format.DateFormat
            .format("yyyy-MM-dd", Date()).toString()

        // 🔹 Get course document
        val courseDoc = db.collection("Courses")
            .document(CourseName)
            .get()
            .await()

        if (!courseDoc.exists()) {
            return "Course not found"
        }

        val allowedBssid = courseDoc.getString("Bssid")
            ?: return "No Wi-Fi assigned to this course"

        val allowedGroups = courseDoc.get("Groups") as? List<String>
            ?: emptyList()

        android.util.Log.d("AttendanceRepository", "Current BSSID: $currentBssid, Allowed BSSID: $allowedBssid")
        // 1 Check Wi-Fi BSSID
        if (!currentBssid.equals(allowedBssid, ignoreCase = true)) {
            return "You are not connected to the lecture Wi-Fi"
        }

        // 2️ Check student group
        if (!allowedGroups.contains(studentGroup)) {
            return "You are not allowed for this group"
        }

        // 3️ Prevent duplicate attendance
        val docId = "$studentId-$today-$CourseName"
        val attendanceRef = db.collection("attendance").document(docId)

        if (attendanceRef.get().await().exists()) {
            return "Attendance already marked"
        }

        // 4️ Save attendance
        val currentUserId = auth.currentUser?.uid ?: "anonymous"

        val data = hashMapOf(
            "name" to name,
            "studentId" to studentId,
            "DoctorName" to DoctorName,
            "group" to studentGroup,
            "course" to CourseName,
            "date" to today,
            "bssid" to currentBssid,
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        attendanceRef.set(data).await()

        return "Attendance marked successfully ✅"
    }


}