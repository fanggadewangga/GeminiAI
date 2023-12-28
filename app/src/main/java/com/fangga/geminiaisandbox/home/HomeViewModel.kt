package com.fangga.geminiaisandbox.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fangga.geminiaisandbox.data.Chat
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val generativeModel: GenerativeModel) : ViewModel() {

    val prompt = mutableStateOf("")
    val chats = mutableListOf<Chat>()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun generateContent(inputText: String) {
        _uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            try {
                val response = generativeModel.generateContent(inputText)
                response.text?.let {
                    _uiState.value = HomeUiState.Success(it)
                    chats.add(Chat(isUser = false, it))
                }
            } catch (e: Exception) {
                _uiState.value =
                    HomeUiState.Error(e.localizedMessage?.toString() ?: "Something went wrong")
            }
        }
    }
}