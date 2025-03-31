package com.pegio.gymbro.presentation.screen.ai_chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.pegio.gymbro.domain.core.onFailure
import com.pegio.gymbro.domain.core.onSuccess
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.ai_chat.GetAiMessagesUseCase
import com.pegio.gymbro.domain.usecase.ai_chat.SaveFireStoreMessagesUseCase
import com.pegio.gymbro.domain.usecase.ai_chat.SendMessageToAiUseCase
import com.pegio.gymbro.domain.usecase.register.GetCurrentUserIdUseCase
import com.pegio.gymbro.presentation.mapper.UiAiMessageMapper
import com.pegio.gymbro.presentation.model.UiAiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendMessageToAi: SendMessageToAiUseCase,
    private val saveMessage: SaveFireStoreMessagesUseCase,
    private val uiAiMessageMapper: UiAiMessageMapper,
    private val getAiMessages: GetAiMessagesUseCase,
    private val fileUploadManager: FileUploadManager,
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
//        observeMessages()
    }

    fun onEvent(event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.OnTextChanged -> _uiState.update { it.copy(inputText = event.text) }
            is AiChatUiEvent.OnSendMessage -> sendMessage(imageUri = event.imageUri)
        }
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

    val test = Pager(
        config = PagingConfig(pageSize = 30),
        pagingSourceFactory = { getAiMessages(uiState.value.userId) }
    )
        .flow
        .map { pagingData -> pagingData.map { uiAiMessageMapper.mapFromDomain(it) } }
        .cachedIn(viewModelScope)

}