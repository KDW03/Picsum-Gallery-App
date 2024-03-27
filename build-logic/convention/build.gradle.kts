// Kotlin 기반의 Gradle 스크립트를 작성할 때 필요한 플러그인
plugins {
    `kotlin-dsl`
}

// Java 소스 코드와 바이너리의 호환성을 Java 17 버전으로 설정
// 빌드된 애플리케이션이나 라이브러리가 Java 17 환경에서 실행댐
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// Kotlin 컴파일러의 JVM 타겟을 Java 17로 설정
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}


// 컴파일 시점에만 필요하며, 런타임에는 포함
dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}


// Gradle 플러그인을 정의
// register 메소드 호출은 하나의 플러그인을 등록하는 데 사용
// 등록한 고유한 ID를 통해 Gradle 빌드 스크립트에서 사용
gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "picsum.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "picsum.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "picsum.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "picsum.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "picsum.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidTest") {
            id = "picsum.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "picsum.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "picsum.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "picsum.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
