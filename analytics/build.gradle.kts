plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.ivangarzab.analytics"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
//    packaging {
//        resources.excludes.add("META-INF/LICENSE.md")
//        resources.excludes.add("META-INF/LICENSE-notice.md")
//    } This block fixed a bug while building, once
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.app.compat)

    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    testImplementation(libs.hilt.testing)
    kaptTest(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso)

    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.agent)
}