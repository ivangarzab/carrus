plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.ivangarzab.carrus"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ivangarzab.carrus"
        minSdk = 26
        targetSdk = 33
        versionCode = 9
        versionName = "1.0.0-alpha09"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
}

val activityVersion = "1.7.2"
val gsonVersion = "2.9.1"
val leakCanaryVersion = "2.12"
dependencies {

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:$activityVersion")
    implementation("androidx.activity:activity-compose:$activityVersion")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.cardview:cardview:1.0.0")
    val navVersion: String by rootProject.extra
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // https://developer.android.com/jetpack/compose/bom
    val composeBom = platform("androidx.compose:compose-bom:2023.05.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-util")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.android.material:material:1.9.0") //TODO: Prob don't need anymore
    implementation("com.google.code.gson:gson:$gsonVersion")

    val accompanistVersion = "0.30.1"
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-webview:$accompanistVersion")

    // https://github.com/google/dagger/tree/master/java/dagger/hilt
    val hiltVersion = "2.44"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // https://firebase.google.com/support/release-notes/android
    implementation(platform("com.google.firebase:firebase-bom:31.1.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // https://github.com/square/leakcanary
    implementation("com.squareup.leakcanary:plumber-android:$leakCanaryVersion")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")

    // https://github.com/JakeWharton/timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // https://github.com/coil-kt/coil
    val coilVersion = "2.4.0"
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")

    // https://github.com/hadilq/LiveEvent
    implementation("com.github.hadilq:live-event:1.3.0")

    // Unit & instrumented testing
    val androidxJunitVersion = "1.1.5"
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.code.gson:gson:$gsonVersion")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.test.ext:junit-ktx:$androidxJunitVersion")
    androidTestImplementation("androidx.test.ext:junit-ktx:$androidxJunitVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

    // https://github.com/robolectric/robolectric
    testImplementation("org.robolectric:robolectric:4.10.3")

    // https://github.com/google/truth
    val truthVersion = "1.1.5"
    testImplementation("com.google.truth:truth:$truthVersion")
    androidTestImplementation("com.google.truth:truth:$truthVersion")

    // https://github.com/mockk/mockk
    val mockkVersion = "1.13.5"
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("io.mockk:mockk-agent:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-agent:$mockkVersion")
}