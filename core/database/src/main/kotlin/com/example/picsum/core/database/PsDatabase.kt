package com.example.picsum.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.picsum.core.database.dao.FeedKeyInfoDao
import com.example.picsum.core.database.dao.FeedsResourceDao
import com.example.picsum.core.database.dao.RecentSearchQueryDao
import com.example.picsum.core.database.entity.FeedKeyInfoEntity
import com.example.picsum.core.database.entity.FeedResourceEntity
import com.example.picsum.core.database.entity.RecentSearchQueryEntity
import com.example.picsum.core.database.util.InstantConverter

@Database(
    entities = [
        FeedResourceEntity::class,
        FeedKeyInfoEntity::class,
        RecentSearchQueryEntity::class
    ],
    version = 1
)
@TypeConverters(
    InstantConverter::class,
)
abstract class PsDatabase : RoomDatabase() {
    abstract val getFeedResourceDao: FeedsResourceDao
    abstract val getFeedKeyInfoDao : FeedKeyInfoDao
    abstract val getRecentSearchQueryDao : RecentSearchQueryDao
}