package com.example.picsum.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

// 최근 검색 query 저장 테이블
// Instant를 활용해 정확한 시각 체크
@Entity(
    tableName = "recentSearchQueries",
)
data class RecentSearchQueryEntity(
    @PrimaryKey
    val query: String,
    @ColumnInfo
    val queriedDate: Instant,
)



