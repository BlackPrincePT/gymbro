package com.pegio.domain.repository

import com.pegio.model.AiMessage
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface AiChatRepository {
    suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network>
}