package com.pegio.gymbro.presentation.ai_chat

import com.pegio.gymbro.domain.core.Error

sealed interface AiChatUiEffect {
    data class Failure(val e: Error) : AiChatUiEffect
}