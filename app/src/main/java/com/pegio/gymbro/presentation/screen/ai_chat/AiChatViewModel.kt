package com.pegio.gymbro.presentation.screen.ai_chat

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LOG_TAG
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.manager.upload.FileUploadManager
import com.pegio.gymbro.domain.usecase.ai_chat.GetAiMessagesUseCase
import com.pegio.gymbro.domain.usecase.ai_chat.SaveFireStoreMessagesUseCase
import com.pegio.gymbro.domain.usecase.ai_chat.SendMessageUseCase
import com.pegio.gymbro.domain.usecase.common.FetchCurrentUserStreamUseCase
import com.pegio.gymbro.domain.usecase.register.GetCurrentUserIdUseCase
import com.pegio.gymbro.domain.usecase.register.SaveUserUseCase
import com.pegio.gymbro.presentation.core.ImageUtil
import com.pegio.gymbro.presentation.mapper.AiChatMessageMapper
import com.pegio.gymbro.presentation.mapper.AiMessageMapper
import com.pegio.gymbro.presentation.mapper.UiUserMapper
import com.pegio.gymbro.presentation.model.UiAiChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val saveFireStoreMessagesUseCase: SaveFireStoreMessagesUseCase,
    private val aiMessageMapper: AiMessageMapper,
    private val aiChatMessageMapper: AiChatMessageMapper,
    private val getAiMessagesUseCase: GetAiMessagesUseCase,
    private val fileUploadManager: FileUploadManager,
    private val imageUtil: ImageUtil,
    private val uiUserMapper: UiUserMapper,
    getCurrentUserId: GetCurrentUserIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AiChatUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        observeMessages()
        getCurrentUserId()?.let { currentUserId ->
            _uiState.update { oldState ->
                oldState.copy(userId = currentUserId)
            }
        }
    }

    fun onEvent(event: AiChatUiEvent) {
        when (event) {
            is AiChatUiEvent.OnTextChanged -> {
                _uiState.value = _uiState.value.copy(inputText = event.text)
            }

            is AiChatUiEvent.OnSendMessage -> sendMessage(imageUri = event.imageUri)
        }
    }


    private fun sendMessage(imageUri: Uri? = null) {
        val text = _uiState.value.inputText
        val newMessages = _uiState.value.messages


        val userMessage = UiAiChatMessage(text = text, isFromUser = true)
        newMessages.add(userMessage)

        _uiState.value =
            _uiState.value.copy(messages = newMessages, inputText = "", isLoading = true)

        val aiMessages = newMessages.map { aiMessageMapper.mapToDomain(it) }

        viewModelScope.launch {
            when (val response = sendMessageUseCase(aiMessages)) {
                is Resource.Success -> {
                    val aiResponse = response.data.text
                    val aiMessage = UiAiChatMessage(text = aiResponse, isFromUser = false)
                    val updatedMessages = newMessages.toMutableList()
                    updatedMessages.add(aiMessage)
                    _uiState.value =
                        _uiState.value.copy(messages = updatedMessages, isLoading = false)
                }

                is Resource.Failure -> {
                    val updatedMessages = newMessages.filterNot { it == userMessage }
                    _uiState.value =
                        _uiState.value.copy(messages = updatedMessages, isLoading = false)
                    _uiEffect.emit(AiChatUiEffect.Failure(response.error))
                }
            }
        }
    }

    private fun observeMessages() = viewModelScope.launch {
        Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { getAiMessagesUseCase(uiState.value.userId) }
        )
            .flow
            .cachedIn(viewModelScope)
            .collectLatest { messagesPagingData ->
                _uiState.update { it.copy(messages = messagesPagingData.map(aiChatMessageMapper::mapFromDomain)) }
            }
    }

    private fun uploadAiMessages(messages: UiAiChatMessage) {
        saveFireStoreMessagesUseCase(
            uiState.value.userId,
            aiChatMessage = aiChatMessageMapper.mapToDomain(messages)
        )
    }
}