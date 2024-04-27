package com.example.picsum

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project

// 각 변형(variant)에 대해 빌드 전에 실행될 로직을 정의
// 현재 변형의 Android 테스트 실행 가능 여부 + src/androidTest 디렉토리의 존재 여부를 통해 테스트 실행 여부를 결정
internal fun LibraryAndroidComponentsExtension.disableUnnecessaryAndroidTests(
    project: Project,
) = beforeVariants {
    it.enableAndroidTest = it.enableAndroidTest
            && project.projectDir.resolve("src/androidTest").exists()
}
