package com.example.picsum.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable
data class BackgroundTheme(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
)

//  CompositionLocal의 인스턴스 BackgroundTheme 객체를 저장
//  Compose 트리 내의 어느 곳에서나 접근 가능
//  LocalBackgroundTheme.current 로 접근가능

// 변화가 적은 데이터이므로 staticCompositionLocalOf 사용
// 그리고 테마 변경은 앱 전체적으로 영향을 미치므로 staticCompositionLocalOf가 훨 이득
val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }
