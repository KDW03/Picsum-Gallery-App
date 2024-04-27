plugins {
    id("picsum.android.feature")
    id("picsum.android.library.compose")
}

android {
    namespace = "com.example.picsum.feature.gallery"
}


dependencies {
    implementation(libs.androidx.paging.compose)
}