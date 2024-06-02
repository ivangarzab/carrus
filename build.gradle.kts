buildscript {
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.nav.safe.args)

        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.secrets.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.gms.google.services) apply false
    alias(libs.plugins.firebase.crashlytics.plugin) apply false
    alias(libs.plugins.androidx.nav.safeargs) apply false
    alias(libs.plugins.android.mapsplatform.secrets) apply false
}