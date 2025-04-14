package com.pegio.presentation.screen.ai_chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pegio.domain.usecase.ObserveAiMessagesPagingStreamUseCase
import com.pegio.domain.usecase.SaveFireStoreMessagesUseCase
import com.pegio.domain.usecase.SendMessageToAiUseCase
import com.pegio.presentation.model.UiAiMessage
import com.pegio.presentation.model.mapper.UiAiMessageMapper
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.domain.usecase.GetCurrentUserIdUseCase
import com.pegio.uploadmanager.core.FileUploadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
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

    var uiState by mutableStateOf(AiChatUiState())
        private set

    private val _uiEffect = MutableSharedFlow<AiChatUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        getCurrentUserId().let { currentUserId ->
            updateState { copy(userId = currentUserId) }
        }
    }

    fun onEvent(event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.OnTextChanged -> updateState { copy(inputText = event.text) }
            is AiChatUiEvent.OnImageSelected -> updateState { copy(selectedImageUri = event.imageUri) }
            is AiChatUiEvent.OnSendMessage -> {
                sendMessage()
                updateState { copy(inputText = "") }
                updateState { copy(selectedImageUri = null) }
            }

            is AiChatUiEvent.LoadMoreMessages -> loadMoreMessages()
            is AiChatUiEvent.OnRemoveImage -> updateState { copy(selectedImageUri = null) }

            // Top Bar
            AiChatUiEvent.OnBackClick -> sendEffect(AiChatUiEffect.NavigateBack)
        }
    }

    private fun loadMoreMessages() {
        // Declare current messages before updating state PRINCE PETER DONT TOUCH IT
        val currentMessages = uiState.messages

        observeAiMessagesPagingStream(
            userId = uiState.userId,
            lastMessageId = uiState.earliestMessageTimestamp
        )
            .onSuccess {
                updateState {
                    copy(
                        messages = it.map(uiAiMessageMapper::mapFromDomain).reversed()
                            .plus(currentMessages),
                        earliestMessageTimestamp = it.lastOrNull()?.timestamp
                    )
                }
            }
            .onFailure {
                sendEffect(AiChatUiEffect.Failure(error = it))
            }
            .launchIn(viewModelScope)
    }


    private fun sendMessage() = viewModelScope.launch {
        val aiMessage = uiAiMessageMapper.mapToDomain(UiAiMessage(text = uiState.inputText))
        val currentUserId = uiState.userId


        uiState.selectedImageUri?.let { uri ->
            fileUploadManager.enqueueFileUpload(uri = uri.toString())
                .onSuccess {
                    saveMessage(
                        userId = currentUserId,
                        aiChatMessage = aiMessage.copy(imageUrl = it)
                    )
                    // Necessary to create artificial sessions
                    updateState {
                        copy(
                            messages = messages + uiAiMessageMapper.mapFromDomain(
                                aiMessage.copy(
                                    imageUrl = it
                                )
                            )
                        )
                    }
                }
        } ?: run {
            saveMessage(userId = currentUserId, aiChatMessage = aiMessage)
            updateState {
                copy(messages = messages + uiAiMessageMapper.mapFromDomain(aiMessage))
            }
        }

        updateState { copy(isLoading = true) }

        sendMessageToAi(aiMessages = uiState.messages.map(uiAiMessageMapper::mapToDomain))
            .onSuccess {
                saveMessage(userId = currentUserId, aiChatMessage = it)
            }
            .onFailure {
                sendEffect(AiChatUiEffect.Failure(error = it))
            }
            .also {
                updateState { copy(isLoading = false) }
            }
    }


    private fun updateState(change: AiChatUiState.() -> AiChatUiState) {
        uiState = uiState.change()
    }

    private fun sendEffect(effect: AiChatUiEffect) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _uiEffect.emit(effect)
        }
    }
}