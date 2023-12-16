buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        val navVersion: String by rootProject.extra
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.nav.safe.args)

        classpath(libs.google.services)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.secrets.gradle.plugin)
    }
}

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.gradle) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}