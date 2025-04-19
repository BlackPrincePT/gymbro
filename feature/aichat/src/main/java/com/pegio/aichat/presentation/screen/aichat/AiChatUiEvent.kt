package com.pegio.aichat.presentation.screen.aichat

import android.net.Uri

sealed interface AiChatUiEvent {
    data class OnTextChanged(val text: String) : AiChatUiEvent
    data class OnImageSelected(val imageUri: Uri) : AiChatUiEvent
    data class OnSendMessage(val imageUri: Uri? = null) : AiChatUiEvent
    data object OnRemoveImage : AiChatUiEvent
    data object LoadMoreMessages: AiChatUiEvent

    // Top Bar
    data object OnBackClick: AiChatUiEvent
}