package com.pegio.aichat.presentation.screen.aichat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.pegio.aichat.presentation.model.UiAiMessage
import com.pegio.aichat.presentation.model.mapper.UiAiMessageMapper
import com.pegio.aichat.presentation.screen.aichat.navigation.AiChatRoute
import com.pegio.aichat.presentation.screen.aichat.state.AiChatUiEffect
import com.pegio.aichat.presentation.screen.aichat.state.AiChatUiEvent
import com.pegio.aichat.presentation.screen.aichat.state.AiChatUiState
import com.pegio.common.core.getOrElse
import com.pegio.common.core.onFailure
import com.pegio.common.core.onSuccess
import com.pegio.common.presentation.core.BaseViewModel
import com.pegio.common.presentation.util.toStringResId
import com.pegio.domain.model.AiChatContext
import com.pegio.domain.usecase.aichat.GetPostContextForAiChatUseCase
import com.pegio.domain.usecase.aichat.ObserveAiMessagesPagingStreamUseCase
import com.pegio.domain.usecase.aichat.SaveFireStoreMessagesUseCase
import com.pegio.domain.usecase.aichat.SendMessageToAiUseCase
import com.pegio.domain.usecase.common.EnqueueFileUploadUseCase
import com.pegio.model.AiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendMessageToAi: SendMessageToAiUseCase,
    private val saveMessage: SaveFireStoreMessagesUseCase,
    private val getPostContextForAiChat: GetPostContextForAiChatUseCase,
    private val observeAiMessagesPagingStream: ObserveAiMessagesPagingStreamUseCase,
    private val enqueueFileUpload: EnqueueFileUploadUseCase,
    private val uiAiMessageMapper: UiAiMessageMapper,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AiChatUiState, AiChatUiEffect, AiChatUiEvent>(initialState = AiChatUiState()) {


    companion object {
        private const val AUTO_POST_QUESTION = "Give me short description for this post"
    }

    private val args = savedStateHandle.toRoute<AiChatRoute>()

    private var aiChatContext: AiChatContext? = null

    init {
        loadMoreMessages()
        args.postId?.let { postId ->
            loadPostContext(postId)
            updateState { copy(inputText = AUTO_POST_QUESTION) }
        }
    }


    override fun onEvent(event: AiChatUiEvent) {
        when (event) {
            // Fields
            is AiChatUiEvent.OnTextChanged -> updateState { copy(inputText = event.text) }

            // Actions
            is AiChatUiEvent.OnImageSelected -> updateState { copy(selectedImageUri = event.imageUri) }
            AiChatUiEvent.OnRemoveImage -> updateState { copy(selectedImageUri = null) }
            is AiChatUiEvent.OnSendMessage -> sendMessage()
            AiChatUiEvent.OnLaunchGallery -> sendEffect(AiChatUiEffect.LaunchGallery)

            // Chat old messages
            AiChatUiEvent.LoadMoreMessages -> loadMoreMessages()

            // Top Bar
            AiChatUiEvent.OnBackClick -> sendEffect(AiChatUiEffect.NavigateBack)
        }
    }

    override fun setLoading(isLoading: Boolean) = updateState { copy(isLoading = isLoading) }

    private fun setPostLoading(isLoading: Boolean) = updateState { copy(isLoadingPost = isLoading) }


    private fun sendMessage() = with(uiState) {
        launchWithLoading {
            val uiMessage = UiAiMessage(text = inputText, isUploading = selectedImageUri != null)

            updateState { copy(inputText = "") }

            var domainMessage = uiAiMessageMapper.mapToDomain(uiMessage)

            selectedImageUri?.let { imageUri ->
                updateState { copy(selectedImageUri = null, messages = listOf(uiMessage) + messages) }

                enqueueFileUpload(imageUri.toString())
                    .onSuccess { domainMessage = domainMessage.copy(imageUrl = it) }
                    .onFailure { sendEffect(AiChatUiEffect.ShowSnackbar(errorRes = it.toStringResId())) }
            }

            saveMessage(aiChatMessage = domainMessage)

            sendAiResponse(newMessage = domainMessage)

        }
    }

    private suspend fun sendAiResponse(newMessage: AiMessage) {
        val domainMessages = uiState.messages.map(uiAiMessageMapper::mapToDomain) + newMessage

        sendMessageToAi(aiMessages = domainMessages, aiChatContext)
            .onFailure { sendEffect(AiChatUiEffect.ShowSnackbar(errorRes = it.toStringResId())) }
            .onSuccess {
                saveMessage(aiChatMessage = it)
                aiChatContext = null
            }
    }

    private fun loadPostContext(postId: String) {
        launchWithLoading(::setPostLoading) {
            aiChatContext = getPostContextForAiChat(postId)
                .getOrElse { return@launchWithLoading }
        }
    }


    private fun loadMoreMessages() {
        val currentMessages = uiState.messages

        observeAiMessagesPagingStream(lastMessageId = uiState.earliestMessageTimestamp)
            .onSuccess { messages ->
                val fetchedMessages = messages.map(uiAiMessageMapper::mapFromDomain)
                updateState {
                    copy(
                        earliestMessageTimestamp = messages.lastOrNull()?.timestamp,
                        messages = currentMessages.plus(fetchedMessages)
                    )
                }
            }
            .onFailure { sendEffect(AiChatUiEffect.ShowSnackbar(errorRes = it.toStringResId())) }
            .launchIn(viewModelScope)
    }
}