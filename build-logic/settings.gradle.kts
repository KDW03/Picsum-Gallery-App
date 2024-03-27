dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    // 의존성과 플러그인의 버전을 중앙에서 관리할 수 있는 TOML 파일을 참조
    versionCatalogs {
        // libs라는 이름의 카탈로그 생성
        create("libs") {
            // 해당 파일로부터 의존성 정보를 가져옴
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
// convention 모듈을 참조
include(":convention")
