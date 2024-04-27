package com.example.picsum

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Compose 구성
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {

    commonExtension.apply {
        // Compose를 활성화
        buildFeatures {
            compose = true
        }

        // Compose 컴파일러의 버전을 설정
        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("androidxComposeCompiler").get().toString()
        }

        // BOM(Bill of Materials)을 사용하여 Compose 관련 의존성의 버전을 관리
        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }

    // 컴파일러 옵션을 구성
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
        }
    }
}


// todo 확인
// Jetpack Compose 컴파일러에 대한 메트릭스 및 보고서 생성을 설정하기 위한 컴파일러 인자
private fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    // Compose 컴파일러 메트릭스를 활성화할지 결정하는 Gradle 프로퍼티를 읽습니다.
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    // 프로젝트 디렉토리와 루트 디렉토리의 상대 경로를 구합니다.
    val relativePath = projectDir.relativeTo(rootDir)

    // 메트릭스를 활성화할 경우
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        // 메트릭스 파일을 저장할 디렉토리 경로를 설정
        val metricsFolder = rootProject.buildDir.resolve("compose-metrics").resolve(relativePath)
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
        )
    }

    // Compose 컴파일러 보고서를 활성화할지 결정하는 Gradle 프로퍼티를 읽는다.
    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = rootProject.buildDir.resolve("compose-reports").resolve(relativePath)
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
        )
    }
    return metricParameters.toList()
}
