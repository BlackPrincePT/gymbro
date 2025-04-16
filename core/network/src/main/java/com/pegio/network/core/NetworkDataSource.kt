package com.pegio.network.core

import com.pegio.model.AiMessage
import com.pegio.network.model.AiChatRequestDto
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource

interface NetworkDataSource {
    suspend fun sendMessage(aiChatRequestDto: AiChatRequestDto): Resource<AiMessage, DataError.Network>

}