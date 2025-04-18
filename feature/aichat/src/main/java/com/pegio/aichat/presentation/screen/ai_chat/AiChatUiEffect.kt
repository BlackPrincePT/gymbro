package com.pegio.aichat.presentation.screen.ai_chat

import androidx.annotation.StringRes

sealed interface AiChatUiEffect {
    data class Failure(@StringRes val errorRes: Int) : AiChatUiEffect

    // Top Bar
    data object NavigateBack: AiChatUiEffect
}