package com.pegio.gymbro.presentation.screen.ai_chat

import androidx.paging.PagingData
import com.pegio.gymbro.presentation.model.UiAiMessage

data class AiChatUiState(
    val userId: String = "",
    val isLoading: Boolean = false,
    val messages: PagingData<UiAiMessage> = PagingData.empty(),
    val inputText: String = ""
)