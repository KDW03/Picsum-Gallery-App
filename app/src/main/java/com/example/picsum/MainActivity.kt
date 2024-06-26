package com.example.picsum

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.picsum.core.data.util.NetworkMonitor
import com.example.picsum.core.model.DarkThemeConfig
import com.example.picsum.core.ui.theme.PsTheme
import com.example.picsum.ui.PsApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: MainActivityViewModel by viewModels()
    private var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSplashScreen()
        setupUiStateObserver()
        enableEdgeToEdge()
        setupContent()
    }

    private fun setupSplashScreen() {
        // SplashScreen 객체가 반환
        val splashScreen = installSplashScreen()
        // 스플래시 스크린 표시 조건, state 상태에 따라
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainActivityUiState.Loading -> true
                is MainActivityUiState.Success -> false
            }
        }
    }

    private fun setupUiStateObserver() {
        // lifecycleOwner에 바인딩 (MainActivity)
        lifecycleScope.launch {
            //특정 생명주기 상태에 도달할 때마다 코루틴 반복 실행 (STARTED 시)
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach { uiState = it }
                    .collect()
            }
        }
    }

    private fun setupContent() {
        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)
            val disableDynamicTheming = shouldDisableDynamicTheming(uiState)
            // darkTheme의 값 변화에 따라 사이드 이펙트
            LaunchedEffect(darkTheme) {
                // ui가 상태바 까지
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
            }
            PsTheme(
                darkTheme = darkTheme,
                disableDynamicTheming = disableDynamicTheming,
            ) {
                when (uiState) {
                    MainActivityUiState.Loading -> Unit
                    is MainActivityUiState.Success -> PsApp(networkMonitor = networkMonitor)
                }
            }
        }
    }

    @Composable
    private fun shouldDisableDynamicTheming(
        uiState: MainActivityUiState,
    ): Boolean = when (uiState) {
        MainActivityUiState.Loading -> false
        is MainActivityUiState.Success -> !uiState.userData.useDynamicColor
    }

    @Composable
    private fun shouldUseDarkTheme(
        uiState: MainActivityUiState,
    ): Boolean = when (uiState) {
        MainActivityUiState.Loading -> isSystemInDarkTheme()
        is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
            DarkThemeConfig.LIGHT -> false
            DarkThemeConfig.DARK -> true
        }
    }
}

private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
