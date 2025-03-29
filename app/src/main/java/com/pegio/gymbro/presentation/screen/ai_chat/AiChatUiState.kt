package com.pegio.gymbro.presentation.screen.ai_chat

import androidx.paging.PagingData
import com.pegio.gymbro.presentation.model.UiAiChatMessage
import com.pegio.gymbro.presentation.model.UiUser

data class AiChatUiState(
    val userId: String = "",
    val isLoading: Boolean = false,
    val messages: PagingData<UiAiChatMessage> = PagingData.empty(),
    val inputText: String = ""
)