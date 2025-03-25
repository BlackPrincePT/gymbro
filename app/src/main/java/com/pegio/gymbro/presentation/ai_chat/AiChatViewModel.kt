package com.pegio.gymbro.presentation.ai_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.usecase.ai_chat.SendMessageUseCase
import com.pegio.gymbro.presentation.mapper.AiMessageMapper
import com.pegio.gymbro.presentation.model.AiChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val aiMessageMapper: AiMessageMapper,
) : ViewModel() {
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

        val userMessage = AiChatMessage(text = text, isFromUser = true)
        newMessages.add(userMessage)

        _uiState.value = _uiState.value.copy(messages = newMessages, inputText = "")

        val aiMessages = newMessages.map { aiMessageMapper.mapToDomain(it) }

        viewModelScope.launch {
            val response = sendMessageUseCase(aiMessages)

            when (response) {
                is Resource.Success -> {
                    val aiResponse = response.data.content
                    val aiMessage = AiChatMessage(text = aiResponse, isFromUser = false)
                    val updatedMessages = newMessages.toMutableList()
                    updatedMessages.add(aiMessage)
                    _uiState.value = _uiState.value.copy(messages = updatedMessages)
                }

                is Resource.Failure -> {
                    val updatedMessages = newMessages.toMutableList()
                    _uiState.value = _uiState.value.copy(messages = updatedMessages)
                }
            }
        }
    }

}