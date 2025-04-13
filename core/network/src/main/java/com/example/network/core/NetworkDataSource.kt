package com.example.network.core

import com.example.model.AiMessage
import com.example.network.model.AiChatRequestDto
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface NetworkDataSource {
    suspend fun sendMessage(aiChatRequestDto: AiChatRequestDto): Resource<AiMessage, DataError.Network>
}