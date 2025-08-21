plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}
android {
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    namespace = "com.homeworks.finalexam"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.homeworks.finalexam"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Unsafe for real apps, but per assignment we embed keys directly
        buildConfigField("String", "TMDB_API_KEY", "\"6edec903b55e26cd9ee1d34452c7c7e2\"")
        buildConfigField("String", "TMDB_BEARER_TOKEN", "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2ZWRlYzkwM2I1NWUyNmNkOWVlMWQzNDQ1MmM3YzdlMiIsIm5iZiI6MTczMjUxODA5Ni40MDUsInN1YiI6IjY3NDQyMGQwODBiNDRhODkzN2I3OWNiMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Is63ZuR1uVq9BllRCuS3FoAw7giMK7nZbxz8hCs5cXc\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}