package com.pegio.data.model.mapper

import com.pegio.model.AiMessage
import com.pegio.network.model.AiChatResponseDto
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