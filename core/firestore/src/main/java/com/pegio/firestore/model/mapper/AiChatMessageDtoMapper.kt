package com.pegio.firestore.model.mapper

import com.pegio.model.AiMessage
import com.pegio.common.core.Mapper
import com.pegio.firestore.model.AiChatMessageDto
import javax.inject.Inject

internal class AiChatMessageDtoMapper @Inject constructor() : Mapper<AiChatMessageDto, AiMessage> {

    override fun mapToDomain(data: AiChatMessageDto): AiMessage {
        return AiMessage(
            id = data.id ?: throw IllegalStateException(),
            text = data.text,
            imageUrl = data.imageUrl,
            isFromUser = data.fromUser,
            timestamp = data.timestamp
        )
    }

    override fun mapFromDomain(data: AiMessage): AiChatMessageDto {
        return AiChatMessageDto(
            id = data.id,
            text = data.text,
            imageUrl = data.imageUrl,
            fromUser = data.isFromUser,
            timestamp = data.timestamp
        )
    }
}