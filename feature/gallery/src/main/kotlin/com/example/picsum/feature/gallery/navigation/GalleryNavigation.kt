package com.example.picsum.feature.gallery.navigation

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