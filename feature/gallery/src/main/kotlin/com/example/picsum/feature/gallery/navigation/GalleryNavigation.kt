package com.example.picsum.feature.gallery.navigation

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.picsum.feature.gallery.GalleryRoute

const val galleryNavigationRoute = "gallery_route"
fun NavController.navigateToGallery(navOptions: NavOptions? = null) {
    this.navigate(galleryNavigationRoute, navOptions)
}

fun NavGraphBuilder.galleryScreen(
    listState: LazyGridState,
    onFeedClick: (Int) -> Unit,
    nestedGraphs: () -> Unit,
) {
    composable(
        route = galleryNavigationRoute,
    ) {
        GalleryRoute(
            listState = listState,
            onFeedClick = onFeedClick
        )
    }
    nestedGraphs()
}