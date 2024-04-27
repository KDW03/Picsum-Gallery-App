package com.example.picsum.core.data.repository

import com.example.picsum.core.model.RecentSearchQuery
import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    fun getRecentSearchQueries(limit: Int = 10): Flow<List<RecentSearchQuery>>
    suspend fun insertOrReplaceRecentSearch(searchQuery: String)
    suspend fun clearRecentSearches()
}
