plugins {
    id("picsum.android.library")
    id("picsum.android.hilt")
}

android {
    namespace = "com.example.picsum.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}