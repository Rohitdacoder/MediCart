plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.example.pov_clone_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pov_clone_project"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    //GoogleMaps
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:maps-compose:2.12.0")
    //OpenSourceMap
    implementation("org.osmdroid:osmdroid-android:6.1.16")
    implementation("androidx.compose.ui:ui:1.0.0")
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.0")
    implementation("androidx.compose.runtime:runtime:1.0.0")


    //Supabase
    implementation("io.ktor:ktor-client-android:2.3.3") // HTTP client for API calls
    // Import the BOM for supabase-kt to manage versions
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.1"))
    // Add the specific modules you need
    implementation("io.github.jan-tennert.supabase:auth-kt:3.1.1")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.1.1")
    implementation("io.github.jan-tennert.supabase:realtime-kt:3.1.1")
    implementation("io.github.jan-tennert.supabase:storage-kt:3.1.1")
    //implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.ktor:ktor-client-core:3.1.1")
    implementation("io.ktor:ktor-client-cio:3.1.1") // CIO engine for networking
    implementation("io.ktor:ktor-client-android:3.1.1") // Android HTTP engine
    implementation("io.ktor:ktor-client-logging:3.1.1") // Optional: Logging for debugging
    implementation("io.ktor:ktor-client-content-negotiation:3.1.1") // JSON handling
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.1") // Serialization
    implementation("io.coil-kt:coil-compose:2.5.0") // For Images
    //implementation("io.supabase:supabase-android:1.0.0")
    implementation("io.ktor:ktor-client-android:3.1.1")
    implementation("io.ktor:ktor-client-okhttp:3.1.1")
    implementation("io.ktor:ktor-client-auth:3.1.1")


    //RazorPay
    implementation("com.razorpay:checkout:1.6.33")

    //DataStore Prefernce
    implementation("androidx.datastore:datastore-preferences:1.0.0")


    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")

    //Firebase
    //Email Signup
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-dynamic-links-ktx")

    //Google Signin
    implementation("com.google.android.gms:play-services-auth:21.1.0")

    val credentialsManagerVersion = "1.5.0"
    implementation("com.google.android.gms:play-services-auth:$credentialsManagerVersion")
    implementation("androidx.credentials:credentials:$credentialsManagerVersion")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.4.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.compose.material:material-icons-extended")


    //Additinal libarires
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}