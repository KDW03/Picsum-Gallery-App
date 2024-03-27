package com.example.picsum

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType



// 버전 카탈로그를 위한 libs 확장 프로퍼티를 정의
// Project 객체에 추가되며, Gradle 스크립트의 다른 부분에서 libs를 통해 접근가능
val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

