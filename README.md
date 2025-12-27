# Attendance App - Project Status & Fixes

## Recent Fixes (Kotlin 2.0 & Compose)

We recently encountered a build error:
> "Starting in Kotlin 2.0, the Compose Compiler Gradle plugin is required when compose is enabled."

**The Solution:**
Kotlin 2.0 moved the Compose Compiler out of the Kotlin compiler itself and into a separate Gradle plugin. We applied the following fix:

1.  **Added the plugin alias** to the root `build.gradle.kts`:
    ```kotlin
    alias(libs.plugins.kotlin.compose) apply false
    ```
2.  **Applied the plugin** in `app/build.gradle.kts`:
    ```kotlin
    alias(libs.plugins.kotlin.compose)
    ```

## Setup Note: `google-services.json`

If you see `app/google-services.json` highlighted in **Red** in Android Studio:
*   **What it means:** The file is **Unversioned** (not tracked by Git).
*   **Impact:** If you push the code, this file will be missing for other teammates.
*   **Action Required:** Right-click the file -> **Git** -> **Add**. It should turn **Green**.

**For Teammates:**
If you clone this repo and the build fails with "File google-services.json is missing", ensure that the file has been committed to the repository or download a fresh copy from the Firebase Console and place it in the `app/` folder.
