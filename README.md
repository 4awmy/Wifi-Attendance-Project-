# Attendance App - Project Documentation

## 1. Project Overview
This project is an Android application built using **Kotlin** and **Jetpack Compose**. It is designed to manage student attendance by validating their physical presence using Wi-Fi BSSID checking. The app follows the **MVVM (Model-View-ViewModel)** architecture and uses **Firebase Firestore** for the backend database and **Firebase Authentication** for user management.

### Architecture: MVVM
- **Model (Data Layer):** Handles data operations with Firebase (Firestore & Auth) and local Wi-Fi scanning.
- **View (UI Layer):** Built with Jetpack Compose, observing state from the ViewModel.
- **ViewModel:** Manages UI state (`StateFlow`) and business logic, acting as a bridge between the View and the Model.

## 2. Team Roles
The development is divided among three key roles:
- **Member A (UI/Compose):** Responsible for building the screens (`LoginScreen`, `CoursesScreen`, `AttendanceScreen`) and navigation.
- **Member B (Repository/ViewModel/Firebase):** Responsible for the data layer (`AttendanceRepository`), business logic (`AttendanceViewModel`), and Firebase integration.
- **Member C (Wi-Fi Logic/Integration):** Responsible for the `WifiScanner` and integrating hardware checks (permissions, BSSID validation).

## 3. Roadmap: 2-Day Crash Plan
- **Day 1 (Core Functionality):**
    - UI Implementation (Login, Courses, Attendance Check).
    - Firebase Connection (Auth & Firestore).
    - Basic Wi-Fi BSSID retrieval and validation.
- **Day 2 (Admin & Polish):**
    - Admin features (if applicable).
    - UI Polish and Error Handling.
    - Testing and Bug Fixes.

---

## 4. Project Structure & File Explanations

### Root Directory
- **`build.gradle.kts`**: The top-level build file. It defines plugins common to all modules (Android Application, Kotlin Android, Google Services) but does not apply them directly (`apply false`). It acts as a central configuration hub for dependency versions.
- **`settings.gradle.kts`**: Defines the project name (`attendance-app`) and includes the `:app` module. It also configures the plugin management repositories (Google, Maven Central, Gradle Plugin Portal).

### App Module (`app/`)
- **`app/build.gradle.kts`**: The module-level build file.
    - Configures the Android SDK versions (compileSdk, minSdk, targetSdk).
    - Enables Jetpack Compose (`buildFeatures { compose = true }`).
    - Declares dependencies: AndroidX Core, Lifecycle, Compose UI/Material3, Firebase (BOM, Auth, Firestore), and Navigation.
- **`app/src/main/AndroidManifest.xml`**: The application manifest.
    - Declares permissions: `INTERNET`, `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE`.
    - Defines the `MainActivity` as the launcher activity.

### Source Code (`app/src/main/java/com/example/attendancewifi/`)

#### **Data Layer (`data/`)**
- **`AttendanceRepository.kt`**:
    - **Purpose:** Acts as the single source of truth for data. It handles all direct interactions with Firebase.
    - **Key Functions:**
        - `loginUser(email, pass)`: Signs in the user using Firebase Auth.
        - `checkAndMarkAttendance(...)`:
            1. Fetches the course document from Firestore ("Courses" collection).
            2. Validates the current Wi-Fi BSSID against the allowed BSSID stored in the course document.
            3. Checks if the student's group is allowed.
            4. Checks for duplicate attendance (using a composite ID: `$studentId-$date-$CourseName`).
            5. Writes the attendance record to the "attendance" collection.
- **`AttendanceUiState.kt`**:
    - **Purpose:** A data class representing the state of the UI at any given moment.
    - **Fields:** `isLoading` (Boolean), `isSuccess` (Boolean), `errorMessage` (String?), `successMessage` (String?). This follows the Unidirectional Data Flow (UDF) pattern.
- **`models/NetworkInfo.kt`**:
    - **Purpose:** A simple data class to hold Wi-Fi information (`ssid`, `bssid`).

#### **Network Layer (`network/`)**
- **`WifiScanner.kt`**:
    - **Purpose:** Handles the retrieval of the device's current Wi-Fi connection details.
    - **Key Function:** `getCurrentNetwork()`: Uses the Android `WifiManager` to get the `connectionInfo` (SSID and BSSID).
    - **Note:** This requires location permissions to work correctly on modern Android versions.

#### **ViewModel Layer (`viewmodel/`)**
- **`AttendanceViewModel.kt`**:
    - **Purpose:** Manages the UI state and holds the business logic. It survives configuration changes.
    - **Key Components:**
        - `_uiState`: A private `MutableStateFlow` that holds the current `AttendanceUiState`.
        - `uiState`: A public read-only `StateFlow` exposed to the UI.
    - **Functions:**
        - `loginUser()`: Calls repository login and updates state (loading -> success/error).
        - `markAttendance()`: Calls repository to check/mark attendance and updates state with the result (success message or error).

#### **UI Layer (`ui/`)**
- **`navigation/AppNavigation.kt`**:
    - **Purpose:** Defines the navigation graph for the app using `NavHost`.
    - **Routes:**
        - `"login"`: Shows `LoginScreen`.
        - `"courses"`: Shows `CoursesScreen`.
        - `"attendance/{courseName}"`: Shows `AttendanceScreen`, passing the selected `courseName` as an argument.

- **`screens/LoginScreen.kt`**:
    - **Purpose:** The entry screen for user authentication.
    - **Features:** Email/Password fields, "Login" button. It observes `uiState` to navigate to "courses" upon success or show an error message.

- **`screens/CoursesScreen.kt`**:
    - **Purpose:** Displays a list of available courses.
    - **Features:** A `LazyColumn` listing hardcoded courses. Clicking a course navigates to the `AttendanceScreen` for that specific course.

- **`screens/AttendanceScreen.kt`**:
    - **Purpose:** The core screen where students mark their attendance.
    - **Logic:**
        1. Checks if Location Permission is granted (required for Wi-Fi scanning).
        2. If granted, it uses `WifiScanner` to get the current BSSID.
        3. Calls `viewModel.markAttendance()` with student details and the retrieved BSSID.
        4. Displays success or error messages based on the result (e.g., "Wrong Wi-Fi", "Attendance marked").

- **`theme/`**: Contains default Compose theming files (`Color.kt`, `Theme.kt`, `Type.kt`).
