package com.example.picsum

// 드 타입을 정의
//applicationIdSuffix를 applicationId에 추가해 같은 기기에서 여러 버전의 앱을 구분하여 설치
enum class DoBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}
