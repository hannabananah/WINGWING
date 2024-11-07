plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jlleitschuh.gradle.ktlint")

    // Hilt를 위한 플러그인들
    kotlin("kapt")  // Kotlin Annotation Processing Tool
    id("com.google.dagger.hilt.android")  // Dependency Injection
}

android {
    namespace = "com.ssafy.shieldroneapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ssafy.shieldroneapp"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            // 디버그 빌드용 API URL
            buildConfigField("String", "BASE_API_URL", "\"https://debug-api.example.com/\"")
            isMinifyEnabled = false
        }
        release {
            // 릴리스 빌드용 API URL
            buildConfigField("String", "BASE_API_URL", "\"https://api.example.com/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Jetpack Compose 설정 추가
    buildFeatures {
        compose = true // Jetpack Compose UI 툴킷 활성화
        buildConfig = true // BuildConfig 활성화
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0" // Kotlin Compiler Extension 버전
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // AndroidX 핵심 라이브러리
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.wearable)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.dagger:hilt-android:2.48")
    implementation ("com.google.code.gson:gson:2.8.9")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // AndroidX Security 라이브러리 (EncryptedSharedPreferences 의존성 추가)
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // Gson 라이브러리
    implementation("com.google.code.gson:gson:2.8.9")

    // HTTP 통신 (API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Jetpack Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform("androidx.compose:compose-bom:2024.01.00")) // BOM(Bill of Materials): Compose 버전 통합 관리
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Compose 확장 라이브러리
    // SystemUiController: 상태바, 네비게이션 바 등 시스템 UI 커스터마이징 (배경색, 아이콘 색상 등)
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Hilt - Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // 테스트
    testImplementation(libs.junit) // 단위 테스트를 위한 기본 라이브러리
    androidTestImplementation(libs.androidx.junit) // 안드로이드 테스트용 JUnit
    androidTestImplementation(libs.androidx.espresso.core) // 안드로이드 테스트용 JUnit
    androidTestImplementation("androidx.compose.ui:ui-test-junit4") // Compose UI 테스트용

    // Wearable 관련 설정
    wearApp(project(":wear"))
}