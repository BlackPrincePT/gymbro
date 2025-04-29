package com.pegio.aichat.presentation.screen.aichat.state

import androidx.annotation.StringRes

sealed interface AiChatUiEffect {

    data class ShowSnackbar(@StringRes val errorRes: Int) : AiChatUiEffect

    data object LaunchGallery : AiChatUiEffect

    // Top Bar
    data object NavigateBack : AiChatUiEffect
}