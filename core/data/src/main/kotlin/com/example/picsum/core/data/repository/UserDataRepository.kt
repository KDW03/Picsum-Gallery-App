package com.example.picsum.core.data.repository

import com.example.picsum.core.model.DarkThemeConfig
import com.example.picsum.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)
}