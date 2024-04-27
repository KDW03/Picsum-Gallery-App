package com.example.picsum.feature.gallery

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.picsum.core.model.FeedResource
import com.example.picsum.core.ui.component.FeedItem

@Composable
internal fun GalleryRoute(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
    onFeedClick: (Int) -> Unit,
    listState: LazyGridState
) {
    val pagingFeeds = viewModel.feedsFlow.collectAsLazyPagingItems()
    val orientation = LocalConfiguration.current.orientation

    GalleryScreen(
        modifier = modifier,
        listState = listState,
        pagingFeeds = pagingFeeds,
        onFeedClick = onFeedClick,
        orientation = orientation
    )
}

@Composable
internal fun GalleryScreen(
    modifier: Modifier,
    pagingFeeds: LazyPagingItems<FeedResource>,
    onFeedClick: (Int) -> Unit,
    orientation: Int,
    listState: LazyGridState
) {
    Box(modifier.fillMaxSize().padding(PaddingValues().calculateTopPadding())) {
        val screenWidthDp = LocalConfiguration.current.orientation
        val columns = calculateColumns(screenWidthDp, orientation)

        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(top = 64.dp ,start = 16.dp , end = 16.dp , bottom = 16.dp),
            modifier = Modifier.testTag("gallery:feed")
        ) {
            items(
                pagingFeeds.itemCount
            ) { index ->
                pagingFeeds[index]?.let { feedResource ->
                    FeedItem(
                        feedResource = feedResource,
                        onFeedClick = onFeedClick,
                        screenWidthDp = screenWidthDp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}



private fun calculateColumns(screenWidth: Int, orientation: Int): Int {
    val baseColumnWidth = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 200 else 150
    val columns = (screenWidth / baseColumnWidth).coerceAtLeast(2)
    return columns.coerceAtMost(6)
}



