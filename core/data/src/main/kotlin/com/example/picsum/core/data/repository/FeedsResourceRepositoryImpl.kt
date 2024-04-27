package com.example.picsum.core.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.example.picsum.core.data.mapper.asEntity
import com.example.picsum.core.data.mapper.asExternalModel
import com.example.picsum.core.data.repository.mediator.PsRemoteMediator
import com.example.picsum.core.database.PsDatabase
import com.example.picsum.core.database.entity.FeedKeyInfoEntity
import com.example.picsum.core.database.entity.FeedResourceEntity
import com.example.picsum.core.model.FeedResource
import com.example.picsum.core.network.PsNetworkDataSource
import com.example.picsum.core.network.util.Dispatcher
import com.example.picsum.core.network.util.PsDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class FeedsResourceRepositoryImpl @Inject constructor(
    private val psDatabase: PsDatabase, private val retrofitPsNetwork: PsNetworkDataSource,
    @Dispatcher(PsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): FeedsResourceRepository {

    override fun getFeeds(): Flow<PagingData<FeedResource>> = Pager(
        // 한 페이지에 로드할 아이템의 수
        config = PagingConfig(PAGE_SIZE),
        // 동기화 처리
        remoteMediator = PsRemoteMediator(
            psDatabase = psDatabase,
            retrofitPsNetwork = retrofitPsNetwork,
        ),
        // PagingSource
        pagingSourceFactory = {
            psDatabase.getFeedResourceDao.getAllFeeds()
        }
    ).flow.map { pagingData -> pagingData.map(FeedResourceEntity::asExternalModel) }

    override suspend fun getQueryFeeds(query: String): Flow<List<FeedResource>> {
        val nextPage = psDatabase.getFeedKeyInfoDao.getLatestKey()?.nextPage

        if (nextPage != null) loadAllDataForQuery(nextPage)

        return try {
            psDatabase.getFeedResourceDao.getQueryFeeds(query)
                .map { entities ->
                    entities.map(FeedResourceEntity::asExternalModel)
                }
        } catch (e: Exception) {
            flowOf(emptyList())
        }
    }
    override suspend fun getFeedById(feedId: Int): Flow<FeedResource> = psDatabase.getFeedResourceDao.getFeedById(feedId).map(FeedResourceEntity::asExternalModel)

    private suspend fun loadAllDataForQuery(nextPage: Int) {
        var currentPage = nextPage
        var shouldContinue = true
        val tasks = mutableListOf<Deferred<Boolean>>()

        withContext(ioDispatcher) {
            while (currentPage <= 100 && shouldContinue) {
                val task = async {
                    try {
                        val response = retrofitPsNetwork.getFeeds(page = currentPage, limit = PAGE_SIZE)
                        val entities = response.feedList

                        if (entities.isNotEmpty()) {
                            psDatabase.withTransaction {
                                psDatabase.getFeedResourceDao.upsertAll(entities.map { it.asEntity() })
                                val keys = entities.map { feed ->
                                    FeedKeyInfoEntity(
                                        id = feed.id.toInt(),
                                        prevPage = response.prevPage,
                                        nextPage = response.nextPage,
                                        lastUpdated = System.currentTimeMillis()
                                    )
                                }
                                psDatabase.getFeedKeyInfoDao.replace(keys)
                            }
                            response.nextPage != null
                        } else {
                            false
                        }
                    } catch (e: Exception) {
                        Log.e("LoadData", "Error : ${e.message}")
                        false // 오류 발생 시 계속 진행하지 않음
                    }
                }
                tasks.add(task)
                currentPage++

                if (tasks.size >= 5) {
                    shouldContinue = tasks.awaitAll().all { it }
                    tasks.clear()
                }
            }

            if (tasks.isNotEmpty()) {
                shouldContinue = tasks.awaitAll().all { it }
            }
        }
    }

}

private const val PAGE_SIZE = 30