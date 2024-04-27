package com.example.picsum.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picsum.core.data.repository.FeedsResourceRepository
import com.example.picsum.feature.gallery.DetailFeedUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailFeedViewModel @Inject constructor(
    private val repository: FeedsResourceRepository
) : ViewModel() {

    private val feedId = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<DetailFeedUiState> = feedId
        .filterNotNull()
        .flatMapLatest { id ->
            repository.getFeedById(id).map(DetailFeedUiState::Success)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    fun setFeedId(id: Int) {
        feedId.value = id
    }
}
