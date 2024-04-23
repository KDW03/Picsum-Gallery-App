package com.example.picsum.core.network.retrofit

import com.example.picsum.core.network.PsNetworkDataSource
import com.example.picsum.core.network.dto.NetworkFeedResource
import com.example.picsum.core.network.dto.NetworkFeedsPagingModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


// PsNetworkDataSource Retrofit Impl

// 하나만 생성
@Singleton
class RetrofitPsNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : PsNetworkDataSource {


    // retrofit 인스턴스 구성
    private val networkApi = Retrofit.Builder()
        .baseUrl(PS_BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitPsNetworkApi::class.java)

    override suspend fun getFeeds(page: Int, limit: Int): NetworkFeedsPagingModel {
        val response = networkApi.getFeeds(page, limit)
        val feedList = response.body().orEmpty()

        val nextPage = getNextPage(response, page)
        val prevPage = getPrevPage(response, page)

        return NetworkFeedsPagingModel(
            currentPage = page,
            nextPage = nextPage,
            prevPage = prevPage,
            feedList = feedList,
        )
    }

    private fun getNextPage(response: Response<List<NetworkFeedResource>>, page: Int): Int? =
        if (response.findNextPage()) page + 1 else null

    private fun getPrevPage(response: Response<List<NetworkFeedResource>>, page: Int): Int? =
        if (response.findPrevPage()) page - 1 else null

    private fun Response<*>.findNextPage(): Boolean =
        headers()[PAGE_HEADER_NAME]?.contains(NEXT_PAGE_NAME) ?: false

    private fun Response<*>.findPrevPage(): Boolean =
        headers()[PAGE_HEADER_NAME]?.contains(PREV_PAGE_NAME) ?: false

    companion object {
        private const val PAGE_HEADER_NAME = "Link"
        private const val NEXT_PAGE_NAME = "next"
        private const val PREV_PAGE_NAME = "prev"
        private const val PS_BASE_URL = "https://picsum.photos"
    }

}