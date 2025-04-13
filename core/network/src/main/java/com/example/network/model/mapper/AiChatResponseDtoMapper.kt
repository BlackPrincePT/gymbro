package com.example.network.model.mapper

import com.example.model.AiMessage
import com.example.network.model.AiChatResponseDto
import com.pegio.common.core.ToDomainMapper
import javax.inject.Inject

class AiChatResponseDtoMapper @Inject constructor() : ToDomainMapper<AiChatResponseDto, AiMessage> {

    override fun mapToDomain(data: AiChatResponseDto): AiMessage {
        return AiMessage(
            id = "",
            text = data.choices.first().message.content,
            imageUrl = null,
            isFromUser = false,
            timestamp = System.currentTimeMillis()
        )
    }
}