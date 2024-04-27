package com.example.picsum.navigation

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.picsum.feature.gallery.navigation.detailFeedScreen
import com.example.picsum.feature.gallery.navigation.galleryNavigationRoute
import com.example.picsum.feature.gallery.navigation.galleryScreen
import com.example.picsum.feature.gallery.navigation.navigateToDetailFeed
import com.example.picsum.feature.navigation.searchScreen
import com.example.picsum.feature.settings.navigation.settingsScreen
import com.example.picsum.ui.PsAppState

@Composable
fun PsNavHost(
    appState: PsAppState,
    modifier: Modifier = Modifier,
    startDestination: String = galleryNavigationRoute,
    listState: LazyGridState,
) {

    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        galleryScreen(
            onFeedClick = navController::navigateToDetailFeed,
            listState = listState
        ) {
            detailFeedScreen()
        }
        searchScreen(
            onBackClick = navController::popBackStack,
            onFeedClick = navController::navigateToDetailFeed,
        )
        settingsScreen()
    }
}