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
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}

plugins {
    alias(libs.plugins.hilt.gradle) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}