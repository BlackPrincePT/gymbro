package com.pegio.gymbro.data.repository

import com.pegio.gymbro.data.remote.api.AiChatApi
import com.pegio.gymbro.data.remote.core.NetworkUtils
import com.pegio.gymbro.data.remote.mapper.AiChatResponseDtoMapper
import com.pegio.gymbro.data.remote.mapper.AiMessageDtoMapper
import com.pegio.gymbro.data.remote.model.AiChatRequestDto
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.map
import com.pegio.gymbro.domain.model.AiChatRequest
import com.pegio.gymbro.domain.model.AiChatResponse
import com.pegio.gymbro.domain.repository.ChatRepository
import javax.inject.Inject

class AiChatRepositoryImpl @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val aiChatApi: AiChatApi,
    private val aiChatResponseDtoMapper: AiChatResponseDtoMapper,
    private val aiMessageDtoMapper: AiMessageDtoMapper
) : ChatRepository {

    override suspend fun sendMessage(aiChatRequest: AiChatRequest): Resource<AiChatResponse, DataError.Network> {
        return networkUtils.handleHttpRequest {
            aiChatApi.sendMessage(
                AiChatRequestDto(
                    model = aiChatRequest.model,
                    messages = aiChatRequest.aiMessages.map { aiMessageDtoMapper.mapFromDomain(it) }
                ))
        }.map {
            aiChatResponseDtoMapper.mapToDomain(it)
        }
    }
}