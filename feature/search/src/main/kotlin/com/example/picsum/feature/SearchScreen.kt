package com.example.picsum.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFeedClick: (Int) -> Unit,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {

}