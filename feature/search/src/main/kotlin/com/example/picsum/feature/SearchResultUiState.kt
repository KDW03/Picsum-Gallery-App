package com.example.picsum.feature

import com.example.picsum.core.model.FeedResource

sealed interface SearchResultUiState {
    data object LoadFailed : SearchResultUiState
    data object NotReady : SearchResultUiState
    data object EmptyResult : SearchResultUiState
    data class Success(
        val feeds: List<FeedResource> = emptyList(),
    ) : SearchResultUiState
}

