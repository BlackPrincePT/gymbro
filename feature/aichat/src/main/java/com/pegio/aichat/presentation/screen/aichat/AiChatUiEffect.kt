package com.pegio.aichat.presentation.screen.aichat

import androidx.annotation.StringRes

sealed interface AiChatUiEffect {
    data class Failure(@StringRes val errorRes: Int) : AiChatUiEffect

    // Top Bar
    data object NavigateBack: AiChatUiEffect
}