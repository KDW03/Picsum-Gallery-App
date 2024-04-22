package com.example.picsum.core.network.dto

data class NetworkFeedsPagingModel(
    val currentPage: Int,
    val nextPage: Int?,
    val prevPage: Int?,
    val feedList: List<NetworkFeedResource>,
)

