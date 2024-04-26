package com.example.picsum.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.picsum.core.data.util.NetworkMonitor
import com.example.picsum.feature.gallery.navigation.galleryNavigationRoute
import com.example.picsum.feature.navigation.navigateToSearch
import com.example.picsum.feature.settings.navigation.navigateToSettings
import com.example.picsum.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPsAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): PsAppState {
    return remember(
        navController,
        coroutineScope,
        networkMonitor,
    ) {
        PsAppState(
            navController,
            coroutineScope,
            networkMonitor,
        )
    }
}

@Stable
class PsAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            galleryNavigationRoute -> TopLevelDestination.GALLERY
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun navigateToSearch() {
        navController.navigateToSearch()
    }

    fun navigateToSetting() {
        navController.navigateToSettings()
    }
}