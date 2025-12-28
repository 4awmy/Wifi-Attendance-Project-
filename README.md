# 📱 Android Attendance App (Firebase + Jetpack Compose)

## 📖 Project Manual & Status Report
**Current State:** Phase 1 Complete (Logic & Data Layer)
**Next Steps:** Phase 2 (UI Implementation & Wi-Fi Integration)

This README explains the entire project structure, the code developed so far, and the remaining tasks for the team.

---

## 🏗 Project Architecture (MVVM)
We are using the **Model-View-ViewModel** pattern to ensure our code is clean, testable, and robust.

1.  **Repository (Model):** Handles data (Firebase).
2.  **ViewModel:** Handles business logic and state.
3.  **UI (View):** Displays data (Jetpack Compose).

---

## 🧑‍💻 Codebase Explanation: What is Developed?

### 1. The Backend Logic (Member B)
The core data handling is **fully implemented**.

*   **`AttendanceRepository.kt`**
    *   **Purpose:** Connects to Firebase Cloud Firestore.
    *   **Key Function:** `markAttendance(studentId, name)`
    *   **Smart Logic:** It generates a unique Document ID (`studentId-date`) to prevent students from submitting duplicate attendance for the same day. It uses `set()` to overwrite, not duplicate.

*   **`AttendanceViewModel.kt`**
    *   **Purpose:** The "Brain" of the app. It connects the UI to the Repository.
    *   **State Management:** It uses `StateFlow` to expose a single `AttendanceUiState` object.
    *   **Logic:** The `submitAttendance()` function coordinates the entire process:
        1.  Checks Wi-Fi validity (passed as a parameter).
        2.  Sets `isLoading = true`.
        3.  Calls the Repository to save to Firebase.
        4.  Updates state to `isSuccess` or `errorMessage`.

*   **`AttendanceUIState.kt`**
    *   **Purpose:** A simple data class holding the screen state (`isLoading`, `isSuccess`, `errorMessage`).

### 2. Build Configuration (DevOps)
We have fixed critical build issues to support **Kotlin 2.0**.
*   **`build.gradle.kts`:** Updated to use the `org.jetbrains.kotlin.plugin.compose` plugin.
*   **`gradle/libs.versions.toml`:** Centralized version management (Version Catalogs).

---

## 🚧 Pending Tasks (The "To-Do" List)

While the logic is ready, the app needs its visible layer and sensors.

### 🔴 Member A: User Interface (Pending)
*   **File:** `MainActivity.kt` (Currently contains default template).
*   **Task:** Build the **Student Check-in Screen**.
*   **Requirements:**
    *   TextFields for Name and Student ID.
    *   "Mark Attendance" Button.
    *   Observe `viewModel.uiState` to show Loading/Success/Error.

### 🔴 Member C: Wi-Fi Logic (Pending)
*   **File:** (New File Needed, e.g., `WifiHelper.kt`).
*   **Task:** Implement BSSID Detection.
*   **Requirements:**
    *   Check Android Permissions (`ACCESS_FINE_LOCATION`).
    *   Get current Wi-Fi BSSID and compare with University BSSID.
    *   Pass the `Boolean` result to the ViewModel.

---

## 🛠 Setup & Integration Guide

### 1. Firebase Setup (Crucial!)
The app requires `google-services.json` to connect to Firebase.
*   **Status:** This file is usually **ignored** by Git for security.
*   **Action:** If `app/google-services.json` is missing or **Red** in Android Studio:
    1.  Download it from the Firebase Console.
    2.  Place it in the `app/` folder.
    3.  Right-click -> **Git** -> **Add**.

### 2. How to Run
1.  Open the project in Android Studio (Ladybug or newer recommended).
2.  Wait for Gradle Sync to finish.
3.  Connect a physical device or Emulator.
4.  Run `app` (Shift+F10).

### 3. Integration Code Snippet
When Member A (UI) and Member C (Wifi) are ready, here is how you connect to Member B's ViewModel in `MainActivity`:

```kotlin
// Inside the Composable
val viewModel: AttendanceViewModel = viewModel()
val uiState by viewModel.uiState.collectAsStateWithLifecycle()

Button(onClick = {
    val isWifiValid = WifiHelper(context).checkWifi() // Member C
    viewModel.submitAttendance(isWifiValid, idInput, nameInput) // Member B
}) {
    Text("Submit")
}
```
