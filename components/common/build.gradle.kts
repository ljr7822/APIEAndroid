plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.xiaoxun.apie.common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.codelocator.core)
    implementation(libs.github.bumptech.glide)
    kapt(libs.github.bumptech.glide.compiler)
    implementation(libs.github.xpopup)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.android.lottie)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.reactivex.rxjava2.rxandroid)
    implementation(libs.okhttp)
    implementation(libs.tencent.mmkv)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.geyifeng.immersionbar)
    implementation(libs.wcdb.room)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.google.code.gson)
    implementation(libs.kotlin.reflect)
    api(libs.datastore)
    api(libs.livedata.ktx)
    api(libs.startup)
    api(libs.easypermissions)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}