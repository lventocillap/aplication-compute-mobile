plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.storecomputer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.storecomputer"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation ("androidx.core:core-ktx:1.6.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("androidx.core:core:1.12.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}