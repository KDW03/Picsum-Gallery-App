package com.example.picsum.feature.gallery.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.picsum.feature.gallery.DetailFeedRoute
import com.example.picsum.feature.gallery.DetailFeedViewModel

fun NavController.navigateToDetailFeed(feedId: Int) {
    this.navigate("Feed_route/$feedId") {
        launchSingleTop = true
    }
}


fun NavGraphBuilder.detailFeedScreen() {
    composable(
        "Feed_route/{feedId}",
        arguments = listOf(navArgument("feedId") { type = NavType.IntType })
    ) { backStackEntry ->
        val viewModel: DetailFeedViewModel = hiltViewModel(backStackEntry)
        val feedId = backStackEntry.arguments?.getInt("feedId") ?: 0
        viewModel.setFeedId(feedId)
        DetailFeedRoute(viewModel = viewModel)
    }
}

