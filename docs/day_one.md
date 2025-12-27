# Attendance App - Day 1 Documentation & Guide

## 📘 Project Overview
This project is an Android Attendance App built with **Jetpack Compose** and **Firebase**. It follows the **MVVM (Model-View-ViewModel)** architecture to separate the UI from the business logic.

---

## 🧑‍💻 Technical Explanation: Logic & Data Layer (Member B's Work)

This section explains the core logic currently implemented in the app. **Member A (UI)** and **Member C (Wi-Fi)** will connect to these components.

### 1. The Repository (`AttendanceRepository.kt`)
The repository is the *only* class that talks directly to Firebase.

*   **Duplicate Prevention:**
    Inside `markAttendance(studentId, name)`, we create a unique document ID:
    ```kotlin
    val docId = "${studentId}-${today}" // e.g., "S12345-2023-10-27"
    ```
    We use `set(data)` instead of `add(data)`. If a student tries to mark attendance twice in one day, it simply **overwrites** the existing document instead of creating a duplicate.

*   **Data Structure:**
    Each attendance record in Firestore looks like this:
    ```json
    {
      "id": "S12345",
      "name": "John Doe",
      "date": "2023-10-27",
      "timestamp": <Firebase Timestamp>
    }
    ```

### 2. The ViewModel (`AttendanceViewModel.kt`)
The ViewModel sits between the UI and the Repository. It holds the **State** of the screen.

*   **UiState (`AttendanceUiState.kt`):**
    A simple data class that describes *everything* the UI needs to render:
    *   `isLoading`: Should we show a loading spinner?
    *   `isSuccess`: Did the upload finish? (Show a "Success" tick).
    *   `errorMessage`: Did something fail? (Show a Toast/Snackbar).

*   **The `submitAttendance` Function:**
    This is the main function Member A's button will call.
    ```kotlin
    fun submitAttendance(isValidWifi: Boolean, studentId: String, name: String)
    ```
    1.  **Wi-Fi Check:** First, it checks `isValidWifi` (which comes from Member C's logic). If false, it sets an error message immediately.
    2.  **Loading:** It sets `isLoading = true`.
    3.  **Firebase:** It calls the Repository.
    4.  **Result:** Updates the state to `isSuccess = true` or sets an `errorMessage`.

---

## 🚧 Day 1 Tasks & Integration Guide

Here is specifically what needs to happen to finish Day 1, and exactly how we will connect our parts together.

### 1. Member A (UI): Building the Screen
**Goal:** Replace the template in `MainActivity.kt` with the actual input screen.

**Integration with Member B (ViewModel):**
You need to "observe" the state I built.
```kotlin
// In MainActivity.kt

// 1. Get the ViewModel
val viewModel: AttendanceViewModel = viewModel()
// 2. Collect the State
val uiState by viewModel.uiState.collectAsStateWithLifecycle()

// 3. Use the state in your UI
if (uiState.isLoading) {
    CircularProgressIndicator()
}

// 4. Call the function on Button Click
Button(onClick = {
    // We will plug in Member C's wifi result here later
    viewModel.submitAttendance(isValidWifi = true, studentId = idText, name = nameText)
}) {
    Text("Mark Attendance")
}
```

### 2. Member C (Wi-Fi): The Logic
**Goal:** Create a helper class/function to check the BSSID.

**Integration with Member B (ViewModel):**
Your logic determines the first parameter of the `submitAttendance` function.

1.  Create a class `WifiHelper(context: Context)`.
2.  Add a function `isValidWifi(): Boolean`.
3.  Inside, use `WifiManager` to get the `connectionInfo.bssid`.
4.  Compare it to our Uni's BSSID (e.g., `"aa:bb:cc:dd:ee:ff"`).

### 3. Putting It Together (The "Handshake")
In `MainActivity.kt`, Member A and C connect like this:

```kotlin
// Member A's UI Button
Button(onClick = {
    // 1. Call Member C's Logic
    val isWifiCorrect = wifiHelper.isValidWifi()

    // 2. Pass result to Member B's ViewModel
    viewModel.submitAttendance(
        isValidWifi = isWifiCorrect,
        studentId = idInput,
        name = nameInput
    )
})
```

---

## 📂 Project Structure Explained

Here is a guide to the other files in the repository so the team understands the setup:

### Build & Configuration
*   **`build.gradle.kts` (Root & App):** These scripts tell Gradle how to build the app.
    *   *Recent Fix:* We added the `kotlin-compose` plugin here to fix the Kotlin 2.0 build error.
    *   *Dependencies:* Libraries like Firebase, Compose, and JUnit are listed here.
*   **`gradle/libs.versions.toml`:** A "Version Catalog". Instead of writing version numbers (like `1.5.0`) inside the build files, we define them all here. This keeps versions consistent across the project.
*   **`gradlew` & `gradlew.bat`:** The "Wrapper" scripts. They allow you to build the project without manually installing Gradle.
*   **`proguard-rules.pro`:** Configuration for code shrinking (minification) when we release the app. Usually ignored during debug.

### Android Essentials
*   **`AndroidManifest.xml`:** The "ID card" of the app. It declares:
    *   Permissions (Internet, Wi-Fi, Location).
    *   The `MainActivity` (the entry point of the app).
*   **`MainActivity.kt`:** The single "Activity" (screen container) for the app. currently, it just holds the basic setup. **Member A** will replace the content here with the Attendance Screen.

### Resources
*   **`res/`:** Contains non-code assets.
    *   `drawable/`: Icons and images.
    *   `values/strings.xml`: Text used in the app (good for translation).
    *   `values/themes.xml`: Colors and styles.

---

## 🛠 Setup Note: `google-services.json`
If you see `app/google-services.json` highlighted in **Red**:
1.  Right-click it.
2.  Select **Git > Add**.
This ensures the file is tracked so everyone can build the app.
