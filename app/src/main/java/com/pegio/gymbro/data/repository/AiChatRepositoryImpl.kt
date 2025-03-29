package com.pegio.gymbro.data.repository

import com.pegio.gymbro.data.remote.api.AiChatApi
import com.pegio.gymbro.data.remote.core.NetworkUtils
import com.pegio.gymbro.data.remote.mapper.AiChatRequestDtoMapper
import com.pegio.gymbro.data.remote.mapper.AiChatResponseDtoMapper
import com.pegio.gymbro.data.remote.model.ContentDto
import com.pegio.gymbro.data.remote.model.MessageDto
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.map
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.ChatRepository
import javax.inject.Inject

class AiChatRepositoryImpl @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val aiChatApi: AiChatApi,
    private val aiChatResponseDtoMapper: AiChatResponseDtoMapper,
    private val aiChatRequestDtoMapper: AiChatRequestDtoMapper
) : ChatRepository {

    override suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network> {
        val aiRequestDto = aiChatRequestDtoMapper.mapFromDomain(aiMessage)
        val messagesWithSystemContext =  listOf(createSystemContext()) + aiRequestDto.messages

        return networkUtils.handleHttpRequest { aiChatApi.sendMessage(aiRequestDto.copy(messages = messagesWithSystemContext)) }
            .map(aiChatResponseDtoMapper::mapToDomain)
    }

    private fun createSystemContext(): MessageDto {
        val textContentDto = ContentDto.TextContentDto(text = "You are gym assistant in app called gymBro")
        return MessageDto(
            role = "system",
            content = listOf(textContentDto)
        )
    }
}