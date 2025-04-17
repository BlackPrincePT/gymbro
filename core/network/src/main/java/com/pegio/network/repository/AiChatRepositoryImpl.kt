package com.pegio.network.repository

import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import com.pegio.domain.repository.AiChatRepository
import com.pegio.model.AiMessage
import com.pegio.network.core.OpenAiConstants
import com.pegio.network.core.OpenAiConstants.SYSTEM
import com.pegio.network.model.ContentDto
import com.pegio.network.model.MessageDto
import com.pegio.network.model.mapper.AiChatRequestDtoMapper
import com.pegio.network.model.mapper.AiChatResponseDtoMapper
import com.pegio.network.service.AiChatService
import com.pegio.network.util.NetworkUtils
import javax.inject.Inject

internal class AiChatRepositoryImpl @Inject constructor(
    private val aiChatService: AiChatService,
    private val aiChatResponseDtoMapper: AiChatResponseDtoMapper,
    private val aiChatRequestDtoMapper: AiChatRequestDtoMapper,
    private val networkUtils: NetworkUtils
): AiChatRepository {

    override suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network> {
        val aiChatRequestDto = aiChatRequestDtoMapper.mapFromDomain(aiMessage)
        val messagesWithSystemContext =  listOf(createSystemContext()).plus(aiChatRequestDto.messages)

        return networkUtils.handleHttpRequest { aiChatService.sendMessage(aiChatRequestDto.copy(messages = messagesWithSystemContext))}
            .map(aiChatResponseDtoMapper::mapToDomain)
    }

    private fun createSystemContext(): MessageDto {
        val textContentDto = ContentDto.TextContentDto(text = OpenAiConstants.DEFAULT_SYSTEM_MESSAGE)

        return MessageDto(role = SYSTEM, content = listOf(textContentDto))
    }
}