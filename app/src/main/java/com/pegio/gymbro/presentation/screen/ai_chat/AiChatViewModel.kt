package com.pegio.gymbro.presentation.screen.ai_chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.gymbro.domain.core.onFailure
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.ai_chat.ObserveAiMessagesPagingStreamUseCase
import com.pegio.gymbro.domain.usecase.ai_chat.SaveFireStoreMessagesUseCase
import com.pegio.gymbro.domain.usecase.ai_chat.SendMessageToAiUseCase
import com.pegio.gymbro.domain.usecase.register.GetCurrentUserIdUseCase
import com.pegio.gymbro.presentation.mapper.UiAiMessageMapper
import com.pegio.gymbro.presentation.model.UiAiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendMessageToAi: SendMessageToAiUseCase,
    private val saveMessage: SaveFireStoreMessagesUseCase,
    private val observeAiMessagesPagingStream: ObserveAiMessagesPagingStreamUseCase,
    private val fileUploadManager: FileUploadManager,
    private val uiAiMessageMapper: UiAiMessageMapper,
    getCurrentUserId: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AiChatUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        getCurrentUserId()?.let { currentUserId ->
            _uiState.update { oldState -> oldState.copy(userId = currentUserId) }
        }
    }

    fun onEvent(event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.OnTextChanged -> _uiState.update { it.copy(inputText = event.text) }
            is AiChatUiEvent.OnSendMessage -> sendMessage(imageUri = event.imageUri)
            AiChatUiEvent.LoadMoreMessages -> loadMoreMessages()
        }
    }

    private fun loadMoreMessages() {
        val currentUserId = _uiState.value.userId
        val currentMessages = _uiState.value.messages

        observeAiMessagesPagingStream(userId = currentUserId, lastMessageId = _uiState.value.earliestMessageTimestamp)
            .onSuccess {
                updateState {
                    copy(
                        messages =  it.map(uiAiMessageMapper::mapFromDomain).reversed().plus(currentMessages),
                        earliestMessageTimestamp = it.lastOrNull()?.timestamp
                    )
                }
            }
            .onFailure { /* TODO: Handle */ }
            .launchIn(viewModelScope)
    }

    private fun sendMessage(imageUri: Uri? = null) = viewModelScope.launch {
        val aiMessage = uiAiMessageMapper.mapToDomain(UiAiMessage(text = uiState.value.inputText))
        val currentUserId = uiState.value.userId

        imageUri?.let {
            fileUploadManager.enqueueFileUpload(uri = imageUri.toString())
                .onSuccess {
                    saveMessage(userId = currentUserId, aiChatMessage = aiMessage.copy(imageUrl = it))
                }
        } ?: run {
            saveMessage(userId = currentUserId, aiChatMessage = aiMessage)
        }

        sendMessageToAi(aiMessages = listOf(aiMessage))
            .onSuccess { saveMessage(userId = currentUserId, aiChatMessage = it) }
            .onFailure { println(it) }
    }

    private fun updateState(state: AiChatUiState.() -> AiChatUiState) {
        _uiState.update(state)
    }

    private fun sendEffect(effect: AiChatUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}