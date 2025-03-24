package com.pegio.gymbro.presentation.ai_chat

import com.pegio.gymbro.presentation.model.ChatMessage

data class AiChatUiState(
    val messages: List<ChatMessage> = listOf(),
    val inputText: String = ""
)