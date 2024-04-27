package com.example.picsum.core.data.mapper

import com.example.picsum.core.database.entity.FeedResourceEntity
import com.example.picsum.core.network.dto.NetworkFeedResource

fun NetworkFeedResource.asEntity() = FeedResourceEntity(
    id = id.toInt(),
    author = author,
    width = width,
    height = height,
    url = url,
    downloadUrl = download_url
)