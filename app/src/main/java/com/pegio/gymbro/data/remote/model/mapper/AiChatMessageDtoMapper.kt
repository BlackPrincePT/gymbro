package com.pegio.gymbro.data.remote.model.mapper

import com.pegio.gymbro.data.remote.model.util.InvalidDocumentIdException
import com.pegio.gymbro.data.remote.model.AiChatMessageDto
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiMessage
import javax.inject.Inject

class AiChatMessageDtoMapper @Inject constructor() : Mapper<AiChatMessageDto, AiMessage> {

    override fun mapToDomain(data: AiChatMessageDto): AiMessage {
        return AiMessage(
            id = data.id ?: throw InvalidDocumentIdException(),
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