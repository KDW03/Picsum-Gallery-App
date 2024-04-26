package com.example.picsum.navigation

import com.example.picsum.feature.gallery.R as galleryR

enum class TopLevelDestination(
    val titleTextId: Int
) {
    GALLERY(
        titleTextId = galleryR.string.gallery
    ),
}