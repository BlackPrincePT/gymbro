package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.AiChatResponseDto
import com.pegio.gymbro.domain.core.ToDomainMapper
import com.pegio.gymbro.domain.model.AiMessage
import javax.inject.Inject

class AiChatResponseDtoMapper @Inject constructor() : ToDomainMapper<AiChatResponseDto, AiMessage> {

    override fun mapToDomain(data: AiChatResponseDto): AiMessage {
        return AiMessage(text = data.choices.first().message.content)
    }
}