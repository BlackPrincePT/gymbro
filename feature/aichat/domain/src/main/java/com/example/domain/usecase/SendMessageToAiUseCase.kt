package com.example.domain.usecase

import com.example.domain.repository.AiChatRepository
import com.example.model.AiMessage
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import javax.inject.Inject

class SendMessageToAiUseCase @Inject constructor(
    private val aiChatRepository: AiChatRepository
)  {
    suspend operator fun invoke(aiMessages: List<AiMessage>): Resource<AiMessage, DataError.Network> {
        return aiChatRepository.sendMessage(aiMessages)
    }
}
