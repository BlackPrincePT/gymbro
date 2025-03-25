package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.AiChatRequestDto
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiChatRequest
import javax.inject.Inject

class AiChatRequestDtoMapper@Inject constructor(
    private val aiMessageDtoMapper: AiMessageDtoMapper

) : Mapper<AiChatRequestDto, AiChatRequest> {
    override fun mapToDomain(data: AiChatRequestDto): AiChatRequest {
        return AiChatRequest(
            model = data.model,
            aiMessages = data.messages.map { aiMessageDtoMapper.mapToDomain(it) }
        )
    }

    override fun mapFromDomain(data: AiChatRequest): AiChatRequestDto {
        return AiChatRequestDto(
            model = data.model,
            messages = data.aiMessages.map { aiMessageDtoMapper.mapFromDomain(it) }
        )
    }
}