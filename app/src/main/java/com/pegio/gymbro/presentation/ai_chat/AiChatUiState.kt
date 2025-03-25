package com.pegio.gymbro.presentation.ai_chat

import com.pegio.gymbro.presentation.model.AiChatMessage

data class AiChatUiState(
    val isLoading: Boolean = false,
    val messages: List<AiChatMessage> = listOf(),
    val inputText: String = ""
)