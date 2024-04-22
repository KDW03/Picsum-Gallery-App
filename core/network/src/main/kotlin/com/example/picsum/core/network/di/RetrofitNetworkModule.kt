package com.example.picsum.core.network.di

import com.example.picsum.core.network.PsNetworkDataSource
import com.example.picsum.core.network.retrofit.RetrofitPsNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


// Network 구현체 DI
@Module
@InstallIn(SingletonComponent::class)
interface RetrofitNetworkModule {
    @Binds
    fun binds(impl: RetrofitPsNetwork): PsNetworkDataSource

}