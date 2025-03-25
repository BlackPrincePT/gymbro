package com.pegio.gymbro.data.remote.mapper

import com.pegio.gymbro.data.remote.model.AiChatResponseDto
import com.pegio.gymbro.data.remote.model.ChoiceDto
import com.pegio.gymbro.data.remote.model.MessageDto
import javax.inject.Inject
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.AiChatResponse

class AiChatResponseDtoMapper @Inject constructor() : Mapper<AiChatResponseDto, AiChatResponse> {

    override fun mapToDomain(data: AiChatResponseDto): AiChatResponse {
        return AiChatResponse(
            content = data.choices.firstOrNull()?.message?.content ?: "No response"
        )
    }

    override fun mapFromDomain(data: AiChatResponse): AiChatResponseDto {
        return AiChatResponseDto(
            choices = listOf(
                ChoiceDto(
                    message = MessageDto(
                        role = "assistant",
                        content = data.content
                    )
                )
            )
        )
    }
}