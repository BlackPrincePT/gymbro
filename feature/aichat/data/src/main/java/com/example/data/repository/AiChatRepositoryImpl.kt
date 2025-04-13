package com.example.data.repository

import com.example.data.core.OpenAiConstants
import com.example.data.core.OpenAiConstants.SYSTEM
import com.example.data.model.mapper.AiChatRequestDtoMapper
import com.example.domain.repository.AiChatRepository
import com.example.model.AiMessage
import com.example.network.core.NetworkDataSource
import com.example.network.model.ContentDto
import com.example.network.model.MessageDto
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