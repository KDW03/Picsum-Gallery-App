package com.example.picsum.feature.gallery

import com.example.picsum.core.model.FeedResource

sealed interface DetailFeedUiState {
    data object Loading : DetailFeedUiState
    data class Success(val feed: FeedResource) : DetailFeedUiState
}