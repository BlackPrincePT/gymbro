package com.pegio.aichat.presentation.screen.aichat

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.pegio.aichat.presentation.model.UiAiMessage
import com.pegio.aichat.presentation.model.mapper.UiAiMessageMapper
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.usecase.aichat.ObserveAiMessagesPagingStreamUseCase
import com.pegio.domain.usecase.aichat.SaveFireStoreMessagesUseCase
import com.pegio.domain.usecase.aichat.SendMessageToAiUseCase
import com.pegio.domain.usecase.common.GetCurrentAuthUserUseCase
import com.pegio.uploadmanager.core.FileUploadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendMessageToAi: SendMessageToAiUseCase,
    private val saveMessage: SaveFireStoreMessagesUseCase,
    private val observeAiMessagesPagingStream: ObserveAiMessagesPagingStreamUseCase,
    private val fileUploadManager: FileUploadManager,
    private val uiAiMessageMapper: UiAiMessageMapper,
    getCurrentAuthUser: GetCurrentAuthUserUseCase
) : BaseViewModel<AiChatUiState, AiChatUiEffect, AiChatUiEvent>(initialState = AiChatUiState()) {


    init {
        getCurrentAuthUser()?.let { user ->
            updateState { copy(userId = user.id) }
        }
    }


    override fun onEvent(event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.OnTextChanged -> updateState { copy(inputText = event.text) }
            is AiChatUiEvent.OnImageSelected -> updateState { copy(selectedImageUri = event.imageUri) }
            is AiChatUiEvent.OnSendMessage -> onSendMessage()
            AiChatUiEvent.LoadMoreMessages -> loadMoreMessages()
            AiChatUiEvent.OnRemoveImage -> updateState { copy(selectedImageUri = null) }

            // Top Bar
            AiChatUiEvent.OnBackClick -> sendEffect(AiChatUiEffect.NavigateBack)
        }
    }

    private fun loadMoreMessages() {
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
                sendEffect(AiChatUiEffect.Failure(errorRes = it.toStringResId()))
            }
            .launchIn(viewModelScope)
    }

    private fun sendMessage() = launchWithLoading {
        val currentUserId = uiState.userId
        val uiMessage = UiAiMessage(
            text = uiState.inputText,
            isUploading = uiState.selectedImageUri != null
        )

        if (uiState.selectedImageUri != null) {
            val pendingMessage = uiMessage.copy(isUploading = true)
            updateState { copy(messages = messages + pendingMessage) }

            handleImageMessage(
                uri = uiState.selectedImageUri!!,
                uiMessage = uiMessage,
                userId = currentUserId
            )
        } else {
            handleTextMessage(uiMessage = uiMessage, userId = currentUserId)
        }

        sendAiResponse(currentUserId)
    }


    private fun handleTextMessage(uiMessage: UiAiMessage, userId: String) {
        val domainMessage = uiAiMessageMapper.mapToDomain(uiMessage)

        saveMessage(userId = userId, aiChatMessage = domainMessage)
        updateState {
            copy(messages = messages + uiMessage)
        }
    }

    private suspend fun handleImageMessage(uri: Uri, uiMessage: UiAiMessage, userId: String) {
        fileUploadManager.enqueueFileUpload(uri.toString())
            .onSuccess { imageUrl ->
                val messageWithImage = uiMessage.copy(imageUrl = imageUrl)
                val domainMessage = uiAiMessageMapper.mapToDomain(messageWithImage)

                saveMessage(userId = userId, aiChatMessage = domainMessage)
                updateState {
                    copy(messages = messages + messageWithImage)
                }
            }
            .onFailure {
                sendEffect(AiChatUiEffect.Failure(errorRes = it.toStringResId()))
                setLoading(false)
            }
    }

    private suspend fun sendAiResponse(userId: String) {
        val domainMessages = uiState.messages.map(uiAiMessageMapper::mapToDomain)

        sendMessageToAi(aiMessages = domainMessages)
            .onSuccess { responseMessage ->
                saveMessage(userId = userId, aiChatMessage = responseMessage)
            }
            .onFailure {
                sendEffect(AiChatUiEffect.Failure(errorRes = it.toStringResId()))
            }
    }

    private fun onSendMessage() {
        sendMessage()
        updateState {
            copy(inputText = "", selectedImageUri = null)
        }
    }

    override fun setLoading(isLoading: Boolean) {
        updateState { copy(isLoading = isLoading) }
    }
}