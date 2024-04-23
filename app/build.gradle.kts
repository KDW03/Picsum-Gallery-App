import com.example.picsum.PsBuildType

plugins {
    id("picsum.android.application")
    id("picsum.android.application.compose")
    id("picsum.android.hilt")
}

android {
    namespace = "com.example.picsum"

    defaultConfig {
        applicationId = "com.example.picsum"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = PsBuildType.DEBUG.applicationIdSuffix
            manifestPlaceholders["appName"] = "picsum (Debug)"
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = PsBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders["appName"] = "picsum"
        }
        create("benchmark") {
            initWith(release)
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles("benchmark-rules.pro")
            isMinifyEnabled = true
            applicationIdSuffix = PsBuildType.BENCHMARK.applicationIdSuffix
            manifestPlaceholders["appName"] = "picsum (Benchmark)"
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":feature:gallery"))
    implementation(project(":feature:search"))
    implementation(project(":feature:settings"))

    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    androidTestImplementation(project(":core:network"))
    testImplementation(project(":core:network"))
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.accompanist.testharness)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.window.manager)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.accompanist.testharness)
    kaptTest(libs.hilt.compiler)
}