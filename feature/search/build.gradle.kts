plugins {
    id("picsum.android.feature")
    id("picsum.android.library.compose")
}

android {
    namespace = "com.example.picsum.feature.settings"
}

dependencies {
    implementation(libs.androidx.paging.compose)
}