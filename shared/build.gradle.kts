plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
        }
        /*commonTest.dependencies {
//            implementation(libs.kotlin.test)
        }*/
    }
}

android {
    namespace = "com.ivangarzab.carrus.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}
