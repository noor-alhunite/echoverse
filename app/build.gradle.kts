plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.echoverse"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.echoverse"
        minSdk = 26
        targetSdk = 36
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
}

dependencies {
    // المكتبات الموجودة
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.games.activity)

    // الحل: إضافة التبعيات باستخدام صيغة Kotlin Script
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ⭐ المكتبات الجديدة من libs.versions.toml ⭐
    implementation(libs.core.ktx)
    implementation(libs.legacy.support)
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.media)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}