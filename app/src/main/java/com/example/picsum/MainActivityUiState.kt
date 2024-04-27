package com.example.picsum

import com.example.picsum.core.model.UserData

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
