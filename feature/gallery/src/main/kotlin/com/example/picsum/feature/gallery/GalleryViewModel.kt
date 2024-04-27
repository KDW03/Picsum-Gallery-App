package com.example.picsum.feature.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.picsum.core.data.repository.FeedsResourceRepository
import com.example.picsum.core.model.FeedResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    feedsRepository: FeedsResourceRepository
) : ViewModel() {

    val feedsFlow: Flow<PagingData<FeedResource>> =
        feedsRepository.getFeeds().cachedIn(viewModelScope)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )
}
