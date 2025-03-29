package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.AiChatMessageDto
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiChatMessage
import javax.inject.Inject

class AiChatMessageDtoMapper @Inject constructor(): Mapper<AiChatMessageDto, AiChatMessage> {
    override fun mapToDomain(data: AiChatMessageDto): AiChatMessage {
        return AiChatMessage(
            text = data.text,
            date = data.date,
            imageUri = data.imageUri,
            isFromUser = data.isFromUser
        )
    }

    override fun mapFromDomain(data: AiChatMessage): AiChatMessageDto {
        return AiChatMessageDto(
            text = data.text,
            date = data.date,
            imageUri = data.imageUri,
            isFromUser = data.isFromUser
        )
    }
}