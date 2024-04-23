package com.example.picsum.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.picsum.core.database.entity.FeedResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedsResourceDao {

    /**
     * upsert : 존재하는 행이면 업데이트 , 없으면 삽입
     * @Insert(onConflict = OnConflictStrategy.REPLACE) 와 동일한 동작
     */
    @Upsert
    suspend fun upsertAll(feeds: List<FeedResourceEntity>)

    @Query("SELECT * FROM feeds_resource WHERE LOWER(author) LIKE '%' || LOWER(:query) || '%'")
    fun getQueryFeeds(query: String): Flow<List<FeedResourceEntity>>

    @Query("SELECT * FROM feeds_resource")
    fun getAllFeeds(): PagingSource<Int, FeedResourceEntity>

    @Query("DELETE FROM feeds_resource")
    suspend fun clearAll()

    @Query("SELECT * FROM feeds_resource WHERE id = :feedId")
    fun getFeedById(feedId: Int): Flow<FeedResourceEntity>
}