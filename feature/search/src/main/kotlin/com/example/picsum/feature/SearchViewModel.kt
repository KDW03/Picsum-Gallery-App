package com.example.picsum.feature

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picsum.core.data.repository.FeedsResourceRepository
import com.example.picsum.core.data.repository.RecentSearchRepository
import com.example.picsum.core.network.util.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.picsum.core.network.util.Result

@HiltViewModel
class SearchViewModel @Inject constructor(
    feedsRepository: FeedsResourceRepository,
    private val recentSearchRepository: RecentSearchRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")


    val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            when {
                query.length < SEARCH_QUERY_MIN_LENGTH -> flowOf(SearchResultUiState.NotReady)
                else -> {
                    feedsRepository.getQueryFeeds(query)
                        .asResult()
                        .map { result ->
                            when (result) {
                                is Result.Success ->
                                    if (result.data.isEmpty()) SearchResultUiState.EmptyResult
                                    else SearchResultUiState.Success(result.data)

                                is Result.Loading -> SearchResultUiState.NotReady
                                is Result.Error -> SearchResultUiState.LoadFailed
                            }
                        }
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResultUiState.NotReady
        )


    val recentSearchQueriesUiState: StateFlow<RecentSearchQueriesUiState> =
        recentSearchRepository.getRecentSearchQueries()
            .map(RecentSearchQueriesUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RecentSearchQueriesUiState.Loading,
            )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun onSearchTriggered(query: String) {
        viewModelScope.launch {
            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery = query)
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            recentSearchRepository.clearRecentSearches()
        }
    }


}


const val SEARCH_QUERY_MIN_LENGTH = 2
private const val SEARCH_QUERY = "searchQuery"