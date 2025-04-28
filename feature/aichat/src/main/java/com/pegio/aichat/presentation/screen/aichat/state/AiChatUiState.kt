package com.pegio.aichat.presentation.screen.aichat.state

import android.net.Uri
import com.pegio.aichat.presentation.model.UiAiMessage

data class AiChatUiState(
    val userId: String = "",
    val isLoading: Boolean = false,
    val isUploading: Boolean = false,
    val messages: List<UiAiMessage> = emptyList(),
    val earliestMessageTimestamp: Long? = Long.MAX_VALUE,
    val inputText: String = "",
    val selectedImageUri: Uri? = null
)