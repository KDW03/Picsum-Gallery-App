plugins {
    id("picsum.android.library")
    id("picsum.android.hilt")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.example.picsum.core.database"
}


protobuf {
    // Protobuf 컴파일러의 경로
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    // Proto 파일 생성 작업들에 java 및 kotlin lite 적용
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.room.paging)
}