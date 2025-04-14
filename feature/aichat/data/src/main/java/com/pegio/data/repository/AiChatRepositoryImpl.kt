package com.pegio.data.repository

import com.pegio.data.core.OpenAiConstants
import com.pegio.data.core.OpenAiConstants.SYSTEM
import com.pegio.data.model.mapper.AiChatRequestDtoMapper
import com.pegio.domain.repository.AiChatRepository
import com.pegio.model.AiMessage
import com.pegio.network.core.NetworkDataSource
import com.pegio.network.model.ContentDto
import com.pegio.network.model.MessageDto
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import javax.inject.Inject

class AiChatRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val aiChatRequestDtoMapper: AiChatRequestDtoMapper
) : AiChatRepository {

    override suspend fun sendMessage(aiMessage: List<AiMessage>): Resource<AiMessage, DataError.Network> {
        val aiRequestDto = aiChatRequestDtoMapper.mapFromDomain(aiMessage)
        val messagesWithSystemContext =  listOf(createSystemContext()).plus(aiRequestDto.messages)

        return networkDataSource.sendMessage(aiRequestDto.copy(messages = messagesWithSystemContext))
    }

    private fun createSystemContext(): MessageDto {
        val textContentDto = ContentDto.TextContentDto(text = OpenAiConstants.DEFAULT_SYSTEM_MESSAGE)

        return MessageDto(role = SYSTEM, content = listOf(textContentDto))
    }


}