package com.example.picsum.feature.gallery

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun GalleryRoute(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
    onFeedClick: (Int) -> Unit,
    listState: LazyGridState
) {


}



