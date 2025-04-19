package com.pegio.aichat.presentation.screen.aichat

import com.pegio.common.core.Error

sealed interface AiChatUiEffect {
    data class Failure(val error: Error) : AiChatUiEffect

    // Top Bar
    data object NavigateBack: AiChatUiEffect
}