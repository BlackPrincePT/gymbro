package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.MessageDto
import javax.inject.Inject
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiMessage

class AiMessageDtoMapper @Inject constructor() : Mapper<MessageDto, AiMessage> {

    override fun mapToDomain(data: MessageDto): AiMessage {
        return AiMessage(
            role = data.role,
            content = data.content
        )
    }

    override fun mapFromDomain(data: AiMessage): MessageDto {
        return MessageDto(
            role = data.role,
            content = data.content
        )
    }
}
