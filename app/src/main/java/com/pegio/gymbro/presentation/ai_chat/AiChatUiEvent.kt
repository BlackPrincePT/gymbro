package com.pegio.gymbro.presentation.ai_chat

sealed class AiChatUiEvent {
    data class OnTextChanged(val text: String) : AiChatUiEvent()
    data object OnSendMessage : AiChatUiEvent()
}