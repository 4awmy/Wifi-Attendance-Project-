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
- **Day 1 (Core Functionality) - Completed:**
    - UI Implementation (Login, Courses, Attendance Check).
    - Firebase Connection (Auth & Firestore).
    - Basic Wi-Fi BSSID retrieval and validation.
- **Day 2 (Admin & Polish) - Completed:**
    - **Role-Based Login:** Routing for Admins, Instructors, and Students.
    - **Admin Dashboard:** Manage courses, instructors, and create students.
    - **Instructor Dashboard:** Spreadsheet-style attendance sheet with synced scrolling.
    - **UI Polish:** Modern Card-based designs.

---

## 4. Project Structure & File Explanations

### Root Directory
- **`build.gradle.kts`**: The top-level build file. Defines common plugins.
- **`settings.gradle.kts`**: Defines the project name (`attendance-app`) and includes the `:app` module.

### App Module (`app/`)
- **`app/build.gradle.kts`**: The module-level build file.
    - Configures Android SDK versions.
    - Declares dependencies: Compose UI/Material3, Firebase (Auth, Firestore), Navigation, and **Extended Material Icons**.

### Source Code (`app/src/main/java/com/example/attendancewifi/`)

#### **Data Layer (`data/`)**
- **`AttendanceRepository.kt`**:
    - **Purpose:** Single source of truth for Firebase interactions.
    - **Key Functions:**
        - `loginUser(email, pass)`: Signs in user.
        - `getUserRole(uid)`: Fetches role ('admin', 'instructor', 'student') from Firestore.
        - `createInstructor(...)` / `createStudent(...)`: Registers users in Auth and Firestore with specific roles.
        - `addCourse(...)`: Creates new course documents.
        - `assignInstructorToCourse(...)`: Links instructors to courses.
        - `checkAndMarkAttendance(...)`: Validates Wi-Fi BSSID and records attendance.
- **`AttendanceUiState.kt`**:
    - **Purpose:** Represents the UI state.
    - **Fields:** `role`, `instructors` list, `courses` list, `attendanceSheet` (for instructors), `isLoading`, messages.
- **`models/` Package:**
    - **`User.kt`**: Data class for Firestore users (includes `role`, `studentId`).
    - **`Course.kt`**: Data class for Courses (includes `instructorId`, `enrolledStudentIds`).
    - **`StudentAttendance.kt`**: Data model for the instructor's attendance sheet view.
    - **`NetworkInfo.kt`**: Holds Wi-Fi SSID/BSSID.

#### **ViewModel Layer (`viewmodel/`)**
- **`AttendanceViewModel.kt`**:
    - **Purpose:** Manages UI state and business logic.
    - **Key Logic:**
        - `loadAdminData()`: Fetches all instructors and courses for the Admin Dashboard.
        - `createStudent(...)`: Calls repository to create a student account.
        - `loadDummyAttendanceSheet()` / `toggleAttendance(...)`: Manages the spreadsheet data for the Instructor Dashboard.

#### **UI Layer (`ui/`)**
- **`navigation/AppNavigation.kt`**:
    - **Purpose:** Defines the navigation graph using `NavHost`.
    - **Routing Logic:**
        - `login` -> Checks role -> Navigates to `admin_dashboard`, `instructor_dashboard`, or `student_home`.
- **`screens/LoginScreen.kt`**:
    - **Purpose:** User authentication. Navigates based on the fetched role.
- **`screens/AdminDashboardScreen.kt`**:
    - **Purpose:** Admin management hub.
    - **Tabs:**
        1. **Manage Courses:** Add courses and assign instructors via dropdown.
        2. **Add Instructor:** Form to create instructor accounts.
        3. **Add Student:** Form to create student accounts.
- **`screens/InstructorDashboardScreen.kt`**:
    - **Purpose:** Instructor view.
    - **Features:** Spreadsheet-style attendance table with **synchronized horizontal scrolling** for dates and checkboxes.
- **`screens/CoursesScreen.kt`**:
    - **Purpose:** Student view.
    - **Design:** Modern **Card-based UI** using `Icons.Default.Book`.
- **`screens/AttendanceScreen.kt`**:
    - **Purpose:** Student attendance marking.
    - **Logic:** Checks Location Permission -> Scans Wi-Fi BSSID -> Submits attendance.

- **`theme/`**: Default Compose theming.
