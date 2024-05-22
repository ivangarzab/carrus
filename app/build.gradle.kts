plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.firebase.crashlytics.plugin)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.nav.safeargs)
    alias(libs.plugins.android.mapsplatform.secrets)
//    alias(libs.plugins.hilt.gradle)
}

android {
    namespace = "com.ivangarzab.carrus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ivangarzab.carrus"
        minSdk = 26
        targetSdk = 34
        versionCode = 17
        versionName = "1.0.0-beta02"

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
        viewBinding = true //TODO: Remove once Activity is migrated into Compose
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
    secrets {
        defaultPropertiesFileName = "default.properties"
    }
}

dependencies {
    implementation(project(":analytics"))
    testImplementation(project(":test-data"))
    androidTestImplementation(project(":test-data"))

    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.app.compat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.cardview)
    implementation(libs.nav.fragment)
    implementation(libs.nav.ui)
    implementation(libs.nav.compose)
    implementation(libs.androidx.lifecycle.extensions)
    testImplementation(libs.androidx.lifecycle.runtime.testing)

    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.lifecycle.runtime)
    implementation(libs.compose.lifecycle.viewmodel)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.util)
    androidTestImplementation(libs.compose.ui.test.junit)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.material)
    implementation(libs.gson)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.webview)

    implementation(libs.play.services.maps)
    implementation(libs.places.ktx)
    implementation(libs.android.maps.utils)
    implementation(libs.android.maps.compose)
    implementation(libs.volley)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)
    implementation(libs.koin.test)
    implementation(libs.koin.test.junit)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.plumber.android)
    debugImplementation(libs.leakcanary.android)

    implementation(libs.timber)

    implementation(libs.coil)
    implementation(libs.coil.compose)

    implementation(libs.live.event)

    // Unit & instrumented testing
    testImplementation(libs.junit)
    testImplementation(libs.gson)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.core.ktx)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.robolectric)

    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)

    testImplementation(libs.mockk)
    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.agent)
}