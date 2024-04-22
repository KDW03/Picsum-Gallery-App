package com.example.picsum.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "feeds_key_info"
)
data class FeedKeyInfoEntity(
    @PrimaryKey
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long
)
