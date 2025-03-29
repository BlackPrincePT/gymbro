package com.pegio.gymbro.presentation.screen.ai_chat

import android.net.Uri

sealed class AiChatUiEvent {
    data class OnTextChanged(val text: String) : AiChatUiEvent()
    data class OnSendMessage(val imageUri: Uri? = null) : AiChatUiEvent()

}