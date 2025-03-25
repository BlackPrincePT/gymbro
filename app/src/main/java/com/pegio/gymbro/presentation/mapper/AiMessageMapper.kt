package com.pegio.gymbro.presentation.mapper

import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.presentation.model.AiChatMessage
import javax.inject.Inject

class AiMessageMapper @Inject constructor() : Mapper<AiChatMessage, AiMessage> {
    override fun mapToDomain(data: AiChatMessage): AiMessage {
        return AiMessage(
            role = if (data.isFromUser) "user" else "assistant",
            content = data.text
        )
    }

    override fun mapFromDomain(data: AiMessage): AiChatMessage {
        return AiChatMessage(
            text = data.content,
            isFromUser = data.role == "user"
        )
    }
}