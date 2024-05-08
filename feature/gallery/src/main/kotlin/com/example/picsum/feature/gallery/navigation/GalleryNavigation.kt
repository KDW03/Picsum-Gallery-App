package com.example.picsum.feature.gallery.navigation

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.MutableState
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
    onFeedClick: (Int) -> Unit,
    isScroll : MutableState<Boolean>,
    nestedGraphs: () -> Unit,
) {
    composable(
        route = galleryNavigationRoute,
    ) {
        GalleryRoute(
            onFeedClick = onFeedClick,
            isScroll = isScroll
        )
    }
    nestedGraphs()
}