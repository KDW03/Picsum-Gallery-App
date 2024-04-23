package com.example.picsum.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.picsum.core.database.entity.FeedKeyInfoEntity

@Dao
interface FeedKeyInfoDao {

    @Query("SELECT * FROM feeds_key_info WHERE id = :id")
    suspend fun getKey(id: Int): FeedKeyInfoEntity?

    @Query("SELECT * FROM feeds_key_info ORDER BY lastUpdated DESC LIMIT 1")
    suspend fun getLatestKey(): FeedKeyInfoEntity?

    @Upsert
    suspend fun replace(pageKeys: List<FeedKeyInfoEntity>)

    @Query("DELETE FROM feeds_key_info")
    suspend fun clearAll()
}