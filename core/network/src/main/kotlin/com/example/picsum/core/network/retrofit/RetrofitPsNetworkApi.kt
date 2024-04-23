package com.example.picsum.core.network.retrofit

import com.example.picsum.core.network.dto.NetworkFeedResource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


// Retrofit API Interface
interface RetrofitPsNetworkApi {

    @GET("/v2/list")
    suspend fun getFeeds(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<NetworkFeedResource>>

}