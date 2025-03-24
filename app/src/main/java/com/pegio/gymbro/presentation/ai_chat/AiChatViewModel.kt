package com.pegio.gymbro.presentation.ai_chat

import androidx.lifecycle.ViewModel
import com.pegio.gymbro.presentation.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()

    fun onEvent(event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.OnTextChanged -> {
                _uiState.value = _uiState.value.copy(inputText = event.text)
            }

            AiChatUiEvent.OnSendMessage -> sendMessage()
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.inputText
        val newMessages = _uiState.value.messages.toMutableList()
        newMessages.add(ChatMessage(text = text, isFromUser = true))
        newMessages.add(ChatMessage(text = "AI Response", isFromUser = false))
        _uiState.value = AiChatUiState(messages = newMessages, inputText = "")
    }
}