package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.AiMessage

interface ChatRepository {
    suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network>
}
