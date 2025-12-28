plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.attendancewifi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.attendancewifi"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // 1. USE THE BOM (Bill of Materials)
    // This effectively "locks" the versions to a stable set compatible with SDK 34
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))

    // 2. STABLE DEPENDENCIES (Remove version numbers from Compose libs)
    implementation("androidx.activity:activity-compose:1.9.0") // Downgraded from 1.12.1
    implementation("androidx.core:core-ktx:1.13.1")            // Downgraded from 1.17.0
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")

    // Compose Libraries (No version numbers needed because of BOM above)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended") // Added for Icons.Default.Book
    implementation("com.google.firebase:firebase-firestore:26.0.2")
    implementation("androidx.compose.material:material-icons-extended")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.material3:material3:1.2.1")

// In D:/AttendanceWifi/app/build.gradle.kts

// Force all Firebase libraries to use versions from this specific, stable BOM
    implementation(enforcedPlatform("com.google.firebase:firebase-bom:33.1.2"))

        // ... other dependencies
        implementation("io.coil-kt:coil-compose:2.7.0") // Check for the latest version


// Add the libraries (without version numbers)implementation("com.google.firebase:firebase-analytics-ktx") // Use the KTX version
    implementation("com.google.firebase:firebase-auth-ktx")     // Use the KTX version
    implementation("com.google.firebase:firebase-firestore-ktx") // Use the KTX version
}