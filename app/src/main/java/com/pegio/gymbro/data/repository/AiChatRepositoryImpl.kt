package com.pegio.gymbro.data.repository

import com.pegio.gymbro.data.remote.api.AiChatApi
import com.pegio.gymbro.data.remote.core.NetworkUtils
import com.pegio.gymbro.data.remote.core.OpenAiConstants.DEFAULT_SYSTEM_MESSAGE
import com.pegio.gymbro.data.remote.core.OpenAiConstants.SYSTEM
import com.pegio.gymbro.data.remote.mapper.AiChatRequestDtoMapper
import com.pegio.gymbro.data.remote.mapper.AiChatResponseDtoMapper
import com.pegio.gymbro.data.remote.model.ContentDto
import com.pegio.gymbro.data.remote.model.MessageDto
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.map
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.AiChatRepository
import javax.inject.Inject

class AiChatRepositoryImpl @Inject constructor(
    private val aiChatApi: AiChatApi,
    private val networkUtils: NetworkUtils,
    private val aiChatResponseDtoMapper: AiChatResponseDtoMapper,
    private val aiChatRequestDtoMapper: AiChatRequestDtoMapper
) : AiChatRepository {

    override suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network> {
        val aiRequestDto = aiChatRequestDtoMapper.mapFromDomain(aiMessage)
        val messagesWithSystemContext =  listOf(createSystemContext()).plus(aiRequestDto.messages)

        return networkUtils.handleHttpRequest { aiChatApi.sendMessage(aiRequestDto.copy(messages = messagesWithSystemContext)) }
            .map(aiChatResponseDtoMapper::mapToDomain)
    }

    private fun createSystemContext(): MessageDto {
        val textContentDto = ContentDto.TextContentDto(text = DEFAULT_SYSTEM_MESSAGE)

        return MessageDto(role = SYSTEM, content = listOf(textContentDto))
    }
}