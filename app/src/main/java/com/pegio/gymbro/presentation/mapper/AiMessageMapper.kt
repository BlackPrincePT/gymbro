package com.pegio.gymbro.presentation.mapper

import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.presentation.model.UiAiChatMessage
import javax.inject.Inject

class AiMessageMapper @Inject constructor() : Mapper<UiAiChatMessage, AiMessage> {

    override fun mapToDomain(data: UiAiChatMessage): AiMessage {
        return AiMessage(
            imageUrl = data.imageUri,
            text = data.text
        )
    }

    override fun mapFromDomain(data: AiMessage): UiAiChatMessage {
        return UiAiChatMessage(
            text = data.text,
            imageUri = data.imageUrl,
            isFromUser = true
        )
    }
}