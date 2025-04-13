package com.example.data.model.mapper

import com.example.model.AiMessage
import com.pegio.common.core.Mapper
import com.pegio.firestore.model.AiChatMessageDto
import javax.inject.Inject

class AiChatMessageDtoMapper @Inject constructor() : Mapper<AiChatMessageDto, AiMessage> {

    override fun mapToDomain(data: AiChatMessageDto): AiMessage {
        return AiMessage(
            id = data.id ?: throw Exception(),
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