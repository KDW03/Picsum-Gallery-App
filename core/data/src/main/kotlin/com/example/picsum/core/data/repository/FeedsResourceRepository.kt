package com.example.picsum.core.data.repository

import androidx.paging.PagingData
import com.example.picsum.core.model.FeedResource
import kotlinx.coroutines.flow.Flow

interface FeedsResourceRepository {
    fun getFeeds() : Flow<PagingData<FeedResource>>
    suspend fun getQueryFeeds(query: String) : Flow<List<FeedResource>>
    suspend fun getFeedById(feedId: Int): Flow<FeedResource>
}