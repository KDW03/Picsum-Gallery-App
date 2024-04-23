package com.example.picsum.core.data.mapper

import com.example.picsum.core.database.entity.FeedKeyInfoEntity
import com.example.picsum.core.database.entity.FeedResourceEntity
import com.example.picsum.core.database.entity.RecentSearchQueryEntity
import com.example.picsum.core.model.FeedKeyInfo
import com.example.picsum.core.model.FeedResource
import com.example.picsum.core.model.RecentSearchQuery

fun RecentSearchQueryEntity.asExternalModel() = RecentSearchQuery(
    query = query,
    queriedDate = queriedDate,
)

fun FeedResourceEntity.asExternalModel() = FeedResource(
    id = id,
    author = author,
    width = width,
    height = height,
    url = url,
    downloadUrl = downloadUrl
)
