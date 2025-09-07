plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")

}

android {
    namespace = "com.example.mechat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mechat"
        minSdk = 24
        targetSdk = 36
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)


    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.analytics.ktx)

    implementation(libs.coil.compose)

    // ADD THESE MISSING AUTHENTICATION DEPENDENCIES
    implementation(libs.play.services.auth)
    implementation(libs.play.services.base)
    implementation(libs.play.services.auth.api.phone)

    // Modern Credential Manager (Google's 2025 recommendation)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

    // IMPORTANT: Core library desugaring for modern Play Services
    coreLibraryDesugaring(libs.desugar.jdk.libs)





//    dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
//    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.navigate)
    ksp(libs.androidx.hilt.compiler)


//    work manager
    implementation(libs.androidx.work.runtime.ktx)

//    room db
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

//    navigation
    implementation(libs.compose.navigation)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)






    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}