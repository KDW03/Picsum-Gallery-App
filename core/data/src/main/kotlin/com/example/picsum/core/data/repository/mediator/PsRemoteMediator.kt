package com.example.picsum.core.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.picsum.core.data.mapper.asEntity
import com.example.picsum.core.database.PsDatabase
import com.example.picsum.core.database.entity.FeedKeyInfoEntity
import com.example.picsum.core.database.entity.FeedResourceEntity
import com.example.picsum.core.network.PsNetworkDataSource
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class PsRemoteMediator(
    private val psDatabase: PsDatabase,
    private val retrofitPsNetwork: PsNetworkDataSource,
) : RemoteMediator<Int, FeedResourceEntity>() {

    // 캐시 타임아웃을 밀리초 단위로 계산하여 저장
    private val cacheTimeOut by lazy { TimeUnit.MILLISECONDS.convert(7, TimeUnit.DAYS) }


    // 데이터 초기화 조건을 설정
    override suspend fun initialize(): InitializeAction {
        // 데이터베이스의 lastKey의 시간을 통해
        val lastUpdateTime = psDatabase.getFeedKeyInfoDao.getLatestKey().getLatestUpdateTime()

        // 데이터베이스에 데이터가 없거나 마지막 업데이트 시간이 7일이 지났다면 새로고침
        return if (lastUpdateTime != null && System.currentTimeMillis() - lastUpdateTime < cacheTimeOut) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedResourceEntity>
    ): MediatorResult {
        try {
            val pageKey = when (loadType) {
                // 새로고침 시 가장 가까운 위치의 키를 조회하여 페이지 결정
                LoadType.REFRESH -> {
                    val remoteKeys = getKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                // 앞쪽 페이지 로딩 시 첫번째 아이템의 키를 조회하여 이전 페이지 결정
                LoadType.PREPEND -> {
                    val remoteKeys = getKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }

                // 뒤쪽 페이지 로딩 시 마지막 아이템의 키를 조회하여 다음 페이지 결정
                LoadType.APPEND -> {
                    val remoteKeys = getKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }
            }

            // 네트워크를 통해 피드 데이터를 요청
            val response = retrofitPsNetwork.getFeeds(page = pageKey, limit = state.config.pageSize)

            psDatabase.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    psDatabase.getFeedKeyInfoDao.clearAll()
                    psDatabase.getFeedResourceDao.clearAll()
                }

                // 응답 데이터를 엔티티로 변환하여 데이터베이스에 저장
                val newEntries = response.feedList.map { it.asEntity() }

                val key = response.feedList.map { feed ->
                    FeedKeyInfoEntity(
                        id = feed.id.toInt(),
                        prevPage = response.prevPage,
                        nextPage = response.nextPage,
                        lastUpdated = System.currentTimeMillis(),
                    )
                }

                psDatabase.getFeedKeyInfoDao.replace(key)
                psDatabase.getFeedResourceDao.upsertAll(newEntries)
            }

            // 페이지가 더 있는지 여부를 반환
            return MediatorResult.Success(endOfPaginationReached = response.feedList.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


    // 현재 위치와 가장 가까운 키를 조회하는 메소드
    private suspend fun getKeyClosestToCurrentPosition(
        state: PagingState<Int, FeedResourceEntity>,
    ): FeedKeyInfoEntity? = state.anchorPosition?.let { position ->
        state.closestItemToPosition(position)?.id?.let { id ->
            psDatabase.getFeedKeyInfoDao.getKey(id)
        }
    }

    // 첫 번째 아이템에 대한 키를 조회하는 메소드
    private suspend fun getKeyForFirstItem(
        state: PagingState<Int, FeedResourceEntity>,
    ): FeedKeyInfoEntity? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.id?.let { id ->
            psDatabase.getFeedKeyInfoDao.getKey(id)
        }

    // 마지막 아이템에 대한 키를 조회하는 메소드
    private suspend fun getKeyForLastItem(
        state: PagingState<Int, FeedResourceEntity>,
    ): FeedKeyInfoEntity? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.id?.let { id ->
            psDatabase.getFeedKeyInfoDao.getKey(id)
        }

    // 키 엔티티에서 마지막 업데이트 시간을 가져오는 메소드
    private fun FeedKeyInfoEntity?.getLatestUpdateTime(): Long? =
        this?.lastUpdated
}