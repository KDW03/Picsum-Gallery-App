package com.example.picsum.feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.picsum.feature.SearchRoute


const val searchRoute = "search_route"

fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(
    onBackClick: () -> Unit,
    onFeedClick: (Int) -> Unit,
) {
    composable(route = searchRoute) {
        SearchRoute(
            onBackClick = onBackClick,
            onFeedClick = onFeedClick,
        )
    }
}
