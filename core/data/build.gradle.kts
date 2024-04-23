plugins {
    id("picsum.android.library")
    id("picsum.android.hilt")
    id("picsum.android.room")
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.picsum.core.data"
}


dependencies {
    //dataSource
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.paging.compose)

    implementation(project(":core:model"))
}