package com.pegio.gymbro.presentation.mapper

import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiChatRequest
import com.pegio.gymbro.presentation.model.AiChatMessage
import javax.inject.Inject

class AiChatMessageMapper @Inject constructor(
    private val aiMessageMapper: AiMessageMapper
) : Mapper<AiChatMessage, AiChatRequest> {
    override fun mapToDomain(data: AiChatMessage): AiChatRequest {
        return AiChatRequest(
            aiMessages = listOf(aiMessageMapper.mapToDomain(data))
        )
    }

    override fun mapFromDomain(data: AiChatRequest): AiChatMessage {
        val aiMessage = data.aiMessages.firstOrNull()
            ?: throw IllegalArgumentException("aiMessage should not be null!!")
        return aiMessageMapper.mapFromDomain(aiMessage)
    }
}