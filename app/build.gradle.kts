plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.unimib.triviaducks"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.unimib.triviaducks"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation (libs.appcompat)
    implementation (libs.material)
    implementation (libs.activity)
    implementation (libs.constraintlayout)
    implementation (libs.commons.validator)
    implementation (libs.jackson.databind)
    implementation (libs.jsoup)
    implementation (libs.viewpager2)
    implementation (libs.material)
    implementation (libs.google.gson)
    implementation (libs.room.runtime)
    implementation (libs.room.compiler)
    implementation (libs.navigation.fragment)
    implementation (libs.navigation.ui)
    implementation (libs.navigation.fragment.ktx)
    implementation (libs.navigation.ui.ktx)
    implementation (libs.glide)
    implementation (libs.converter.gson)
    implementation (libs.retrofit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}