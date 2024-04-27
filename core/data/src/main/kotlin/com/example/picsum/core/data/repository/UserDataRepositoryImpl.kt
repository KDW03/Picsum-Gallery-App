package com.example.picsum.core.data.repository

import com.example.picsum.core.datastore.PsPreferencesDataSource
import com.example.picsum.core.model.DarkThemeConfig
import com.example.picsum.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


// psPreferencesDataSource는 인터페이스나 추상 클래스를 구현하지 않는 일반 클래스이기에 @Module @Binds 필요 없음
class UserDataRepositoryImpl @Inject constructor(
    private val psPreferencesDataSource: PsPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        psPreferencesDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        psPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        psPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

}
