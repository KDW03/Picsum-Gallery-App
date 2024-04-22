package com.example.picsum.core.network

import com.example.picsum.core.network.dto.NetworkFeedsPagingModel



// network Data Source Interface
interface PsNetworkDataSource {
    suspend fun getFeeds(page: Int, limit: Int): NetworkFeedsPagingModel

}