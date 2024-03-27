
// 플러그인 설정 검색 관리
pluginManagement {
    // 현재 Gradle 빌드에 "build-logic" 빌드 로직을 포함
    includeBuild("build-logic")
    repositories {
        //  Google이 제공하는 Android 및 Google 관련 플러그인을 포함
        google {
            // includeGroupByRegex를 사용해 com.android.*, com.google.*, androidx.*로 시작하는 그룹만 포함
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Maven Central 저장소에서 플러그인을 검색
        mavenCentral()
        // Gradle 플러그인 포털에서 플러그인을 검색
        gradlePluginPortal()
    }
}

// 프로젝트 전체에서 사용되는 라이브러리 의존성 리포지터리 관리 todo 삭제 해보기
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// 루트 프로젝트 이름
rootProject.name = "Picsum"
// 빌드에 포함할 서브 프로젝트
include(":app")
 