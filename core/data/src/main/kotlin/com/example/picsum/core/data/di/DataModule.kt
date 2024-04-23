package com.example.picsum.core.data.di

import com.example.picsum.core.data.repository.FeedsResourceRepository
import com.example.picsum.core.data.repository.FeedsResourceRepositoryImpl
import com.example.picsum.core.data.repository.RecentSearchRepository
import com.example.picsum.core.data.repository.RecentSearchRepositoryImpl
import com.example.picsum.core.data.repository.UserDataRepository
import com.example.picsum.core.data.repository.UserDataRepositoryImpl
import com.example.picsum.core.data.util.NetworkMonitor
import com.example.picsum.core.data.util.NetworkMonitorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl,
    ): UserDataRepository

    @Binds
    fun bindsNetworkMonitor(
        networkMonitor: NetworkMonitorImpl,
    ): NetworkMonitor

    @Binds
    fun bindsFeedsResourceRepository(
        feedsResourceRepository: FeedsResourceRepositoryImpl,
    ): FeedsResourceRepository

    @Binds
    fun bindsRecentSearchRepository(
        recentSearchRepository: RecentSearchRepositoryImpl,
    ): RecentSearchRepository

}