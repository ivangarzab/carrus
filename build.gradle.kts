buildscript {
    extra.apply {
        set("navVersion", "2.5.3")
    }

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        val navVersion: String by rootProject.extra
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")

        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}

plugins {
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}