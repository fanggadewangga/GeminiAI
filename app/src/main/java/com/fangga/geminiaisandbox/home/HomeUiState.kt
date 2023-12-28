package com.fangga.geminiaisandbox.home

sealed interface HomeUiState {
    data object Initial: HomeUiState
    data object Loading: HomeUiState
    data class Success(val output: String): HomeUiState
    data class Error(val errorMessage: String): HomeUiState
}