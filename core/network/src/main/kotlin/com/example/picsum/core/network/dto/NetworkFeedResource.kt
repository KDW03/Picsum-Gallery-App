package com.example.picsum.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class NetworkFeedResource(
    val id: String,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val download_url: String
)