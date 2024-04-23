package com.example.picsum.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.picsum.core.database.PsDatabase
import com.example.picsum.core.database.dao.FeedKeyInfoDao
import com.example.picsum.core.database.dao.FeedsResourceDao
import com.example.picsum.core.database.dao.RecentSearchQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesPsDatabase(
        @ApplicationContext context: Context,
    ): PsDatabase = Room.databaseBuilder(
        context,
        PsDatabase::class.java,
        "ps-database",
    ).build()

    @Provides
    fun providesFeedsResourceDao(
        database: PsDatabase,
    ): FeedsResourceDao = database.getFeedResourceDao

    @Provides
    fun providesFeedsKeyInfoDao(
        database: PsDatabase,
    ): FeedKeyInfoDao = database.getFeedKeyInfoDao

    @Provides
    fun providesRecentSearchQueryDao(
        database: PsDatabase,
    ): RecentSearchQueryDao = database.getRecentSearchQueryDao

}