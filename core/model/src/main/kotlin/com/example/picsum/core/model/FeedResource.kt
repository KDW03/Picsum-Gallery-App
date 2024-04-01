package com.example.picsum.core.model

data class FeedResource(
    val id: Int,
    val author: String,
    val width: Int,
    val height: Int,
    val url: String,
    val downloadUrl: String
)