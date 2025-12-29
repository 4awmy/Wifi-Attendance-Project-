package com.example.attendancewifi.data

import com.example.attendancewifi.data.models.Course
import com.example.attendancewifi.data.models.User
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

    suspend fun getUserRole(uid: String): String {
        val document = db.collection("Users").document(uid).get().await()
        if (!document.exists()) {
            throw Exception("Account setup incomplete: No role assigned")
        }
        return document.getString("role") ?: throw Exception("Account setup incomplete: No role assigned")
    }

    // --- Admin Features ---

    suspend fun createInstructor(email: String, pass: String, name: String) {
        // Create user in Firebase Auth
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        val uid = result.user?.uid ?: throw Exception("Instructor creation failed")

        // Write to Firestore Users collection
        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "role" to "instructor"
        )
        db.collection("Users").document(uid).set(userMap).await()
    }

    suspend fun addCourse(courseName: String) {
        val courseMap = hashMapOf(
            "courseName" to courseName,
            "instructorId" to "",
            "enrolledStudentIds" to emptyList<String>()
        )
        // Document ID is the courseName itself
        db.collection("Courses").document(courseName).set(courseMap).await()
    }

    suspend fun assignInstructorToCourse(courseName: String, instructorId: String) {
        db.collection("Courses").document(courseName)
            .update("instructorId", instructorId)
            .await()
    }

    suspend fun getAllInstructors(): List<User> {
        val snapshot = db.collection("Users")
            .whereEqualTo("role", "instructor")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val uid = doc.id
            val name = doc.getString("name") ?: "Unknown"
            val email = doc.getString("email") ?: ""
            val role = doc.getString("role") ?: "instructor"
            User(uid, name, email, role)
        }
    }

    suspend fun getAllCourses(): List<Course> {
        val snapshot = db.collection("Courses").get().await()
        return snapshot.documents.mapNotNull { doc ->
            val id = doc.id // courseName is the ID
            val name = doc.getString("courseName") ?: id
            val instructorId = doc.getString("instructorId") ?: ""
            val enrolled = doc.get("enrolledStudentIds") as? List<String> ?: emptyList()
            Course(id, name, instructorId, enrolled)
        }
    }

    suspend fun createStudent(email: String, password: String, name: String, studentId: String) {
        // 1. Create Auth User
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: return
        // 2. Create User Object
        val newUser = User(
            uid = uid,
            name = name,
            email = email,
            role = "student",
            studentId = studentId
        )
        // 3. Save to Firestore "Users" collection
        db.collection("Users").document(uid).set(newUser).await()
    }

    suspend fun getAllStudents(): List<User> {
        val snapshot = db.collection("Users")
            .whereEqualTo("role", "student")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val uid = doc.id
            val name = doc.getString("name") ?: "Unknown"
            val email = doc.getString("email") ?: ""
            val role = doc.getString("role") ?: "student"
            val studentId = doc.getString("studentId")
            User(uid, name, email, role, studentId)
        }
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