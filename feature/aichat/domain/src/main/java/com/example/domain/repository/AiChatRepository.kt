package com.example.domain.repository

import com.example.model.AiMessage
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface AiChatRepository {
    suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network>
}
