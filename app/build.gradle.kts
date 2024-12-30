import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
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

        resValue("bool", "debug_mode", gradleLocalProperties(rootDir, providers).getProperty("debug_mode"))
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
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.commons.validator)
    implementation(libs.constraintlayout)
    implementation(libs.converter.gson)
    implementation(libs.google.gson)
    implementation(libs.jackson.databind)
    implementation(libs.jsoup)
    implementation(libs.lottie)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.retrofit)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.viewpager2)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.database)

    testImplementation(libs.junit)

    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

}