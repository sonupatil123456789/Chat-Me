//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.kotlin.compose) apply false
//
//    id("com.google.gms.google-services") version "4.4.3" apply false
//    id("com.google.dagger.hilt.android") version "2.56" apply false
//    alias(libs.plugins.kotlinx.serialization.json) apply false
//    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false
//
//}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // UPDATED VERSION (your 4.4.3 is good, but ensure consistency)
    id("com.google.gms.google-services") version "4.4.3" apply false
    id("com.google.dagger.hilt.android") version "2.56" apply false
    alias(libs.plugins.kotlinx.serialization.json) apply false

    // UPDATED KSP VERSION for compatibility
    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false

}
