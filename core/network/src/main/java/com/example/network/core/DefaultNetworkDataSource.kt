package com.example.network.core

import com.example.model.AiMessage
import com.example.network.model.AiChatRequestDto
import com.example.network.model.mapper.AiChatResponseDtoMapper
import com.example.network.service.AiChatService
import com.example.network.util.NetworkUtils
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultNetworkDataSource @Inject constructor(
    private val aiChatService: AiChatService,
    private val aiChatResponseDtoMapper: AiChatResponseDtoMapper,
    private val networkUtils: NetworkUtils
): NetworkDataSource {

    override suspend fun sendMessage(aiChatRequestDto: AiChatRequestDto): Resource<AiMessage, DataError.Network> {
        return networkUtils.handleHttpRequest { aiChatService.sendMessage(aiChatRequestDto)}
            .map(aiChatResponseDtoMapper::mapToDomain)
    }
}