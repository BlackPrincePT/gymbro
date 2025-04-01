package com.pegio.gymbro.presentation.screen.ai_chat

import com.pegio.gymbro.presentation.model.UiAiMessage

data class AiChatUiState(
    val userId: String = "",
    val isLoading: Boolean = false,
    val messages: List<UiAiMessage> = emptyList(),
    val earliestMessageTimestamp: Long? = Long.MAX_VALUE,
    val inputText: String = ""
)