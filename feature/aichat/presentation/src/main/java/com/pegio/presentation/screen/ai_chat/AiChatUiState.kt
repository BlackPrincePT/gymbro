package com.pegio.presentation.screen.ai_chat

import android.net.Uri
import com.pegio.presentation.model.UiAiMessage

data class AiChatUiState(
    val userId: String = "",
    val isLoading: Boolean = false,
    val messages: List<UiAiMessage> = emptyList(),
    val earliestMessageTimestamp: Long? = Long.MAX_VALUE,
    val inputText: String = "",
    val selectedImageUri: Uri? = null
)