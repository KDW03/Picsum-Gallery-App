package com.example.picsum.core.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

class NetworkMonitorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {

    override val isOnline: Flow<Boolean> = callbackFlow {
        // 네트워크 상태를 감지하기 위한 ConnectivityManager 서비스
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        // null인 경우 false 후 종료
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        // 네트워크 상태 변경을 감지하는 콜
        val callback = object : ConnectivityManager.NetworkCallback() {

            // Network 인스턴스를 저장 (WIFI , 데이터)
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                // 사용할 수 있는 네트워크가 있는지
                channel.trySend(networks.isNotEmpty())
            }
        }

        // 네트워크 요청 객체 (사용가능한 네트워크만)
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // 초기 현재 장치의 네트워크 연결 상태를 체크
        channel.trySend(connectivityManager.isCurrentlyConnected())

        // callbackFlow가 취소되거나 완료될 때 호출
        // 콜백 해제
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }

        // 가장 최근의 값을 유지하면서 이전에 방출된 값들을 무시
        // 가장 최근에 방출된 값만을 전달
    }.conflate()


    // 현재 활성화된 네트워크의 Network 객체를 반환
    // 현재 activeNetwork의 Capabilities객체가 네트워크가 인터넷 접근 권한(NET_CAPABILITY_INTERNET)을 가지고 있는지
    private fun ConnectivityManager.isCurrentlyConnected() = activeNetwork
        ?.let(::getNetworkCapabilities)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
}