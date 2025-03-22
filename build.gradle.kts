// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.google.firebase.firebase-perf") version "1.4.2" apply false

    alias(libs.plugins.gms) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.compose.compiler) apply false

}