package com.example.picsum.core.model

data class FeedKeyInfo (
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long
)