package com.pegio.aichat.presentation.screen.aichat.state

import android.net.Uri

sealed interface AiChatUiEvent {
    // Actions
    data class OnImageSelected(val imageUri: Uri) : AiChatUiEvent
    data object OnRemoveImage : AiChatUiEvent
    data class OnSendMessage(val imageUri: Uri? = null) : AiChatUiEvent

    // Fields
    data class OnTextChanged(val text: String) : AiChatUiEvent

    // Chat old messages
    data object LoadMoreMessages: AiChatUiEvent

    // Top Bar
    data object OnBackClick: AiChatUiEvent
}