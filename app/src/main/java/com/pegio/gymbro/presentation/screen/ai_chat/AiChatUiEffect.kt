package com.pegio.gymbro.presentation.screen.ai_chat

import com.pegio.gymbro.domain.core.Error

sealed interface AiChatUiEffect {
    data class Failure(val error: Error) : AiChatUiEffect

    // Top Bar
    data object NavigateBack: AiChatUiEffect
}