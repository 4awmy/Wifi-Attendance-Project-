# Attendance App - Project Status & Roadmap

## 📊 Current Implementation Status
**Last Updated:** Phase 1 (Core Logic Implementation)

We have successfully set up the project structure and implemented the **Data & Logic Layers**. The UI is currently in the initial template state.

### ✅ Completed Modules
*   **Repository Layer (Member B):**
    *   `AttendanceRepository.kt`: Implemented. Connects to Firebase Firestore.
    *   **Duplicate Prevention:** Uses `docId = "$studentId-$today"` to ensure only one submission per day.
    *   **Data Model:** Saves `name`, `id`, `date`, and `timestamp`.
*   **ViewModel Layer (Member B):**
    *   `AttendanceViewModel.kt`: Implemented. Handles business logic.
    *   **State Management:** Uses `StateFlow` (`AttendanceUiState`) for Loading, Success, and Error states.
    *   **Logic:** `submitAttendance()` function is ready to accept Wi-Fi validation results.
*   **Build Configuration:**
    *   Fixed Kotlin 2.0 / Compose Compiler build errors.
    *   `google-services.json` support verified (must be added manually by teammates).

### 🚧 Pending / In-Progress Modules
*   **User Interface (Member A):**
    *   `MainActivity.kt` currently contains default template code.
    *   **Needs:** Student Check-in Screen (Inputs for Name/ID, "Check In" Button, Status Messages).
*   **Wi-Fi Logic (Member C):**
    *   **Needs:** `WifiHelper` class or logic to detect BSSID/SSID.
    *   **Needs:** Android Permission handling (Location/Wi-Fi).

---

## 🚀 Remaining Roadmap (2-Day Crash Plan)

Based on our instructor-aligned timeline, here is what is left to achieve in the next 48 hours.

### 🟦 DAY 1: Connect UI & Wi-Fi (Immediate Priority)

#### 👤 Member A: UI Implementation
*   **Task:** Replace `MainActivity` template with the **Student Attendance Screen**.
*   **Requirements:**
    *   Two TextFields (Name, Student ID).
    *   One Button ("Mark Attendance").
    *   Observe `viewModel.uiState` to show a ProgressBar (Loading) or Toast/Text (Success/Error).

#### 👤 Member C: Wi-Fi Validation
*   **Task:** Implement Wi-Fi detection logic.
*   **Requirements:**
    *   Request `ACCESS_FINE_LOCATION` logic.
    *   Create a function `isValidWifi(): Boolean`.
    *   *Logic:* Check if the phone is connected to the specific University BSSID/SSID.
    *   Pass the result to `viewModel.submitAttendance(isValidWifi, ...)`

---

### 🟩 DAY 2: Admin View & Polish

#### 👤 Member A & B: Admin Dashboard
*   **Task:** Create a simple Admin List to view today's attendance.
*   **Implementation:**
    *   **Repo:** Add `getTodayAttendance()` using a Firestore SnapshotListener (Real-time).
    *   **UI:** Add a second screen (or toggle) with a `LazyColumn` to display the list.

#### 🏁 Final Polish
*   **Error Handling:** Show friendly messages ("Connect to Uni Wi-Fi", "Permission Denied").
*   **Testing:** Verify on 2 physical devices.
*   **Documentation:** Take screenshots for the final submission.

---

## 🛠 Setup Instructions for Teammates
1.  **Clone the Repo.**
2.  **Add Firebase Config:** Download `google-services.json` from Firebase Console and place it in `app/`. (See *Git Note* below).
3.  **Build:** Run `./gradlew assembleDebug`.

### ⚠️ Git Note: `google-services.json`
If this file is **Red** in Android Studio, right-click it and select **Git > Add**. It must be part of the repo for everyone to build.
