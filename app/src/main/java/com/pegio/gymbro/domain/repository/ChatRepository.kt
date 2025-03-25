package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.AiChatRequest
import com.pegio.gymbro.domain.model.AiChatResponse

interface ChatRepository {
    suspend fun sendMessage(aiChatRequest: AiChatRequest): Resource<AiChatResponse, DataError.Network>
}
