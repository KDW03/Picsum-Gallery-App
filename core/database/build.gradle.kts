plugins {
    id("picsum.android.library")
    id("picsum.android.hilt")
    id("picsum.android.room")
}

android {
    namespace = "com.example.picsum.core.database"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.room.paging)
}