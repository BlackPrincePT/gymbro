package com.pegio.gymbro.domain.usecase.ai_chat

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.AiChatRepository
import javax.inject.Inject

class SendMessageToAiUseCase @Inject constructor(
    private val aiChatRepository: AiChatRepository
)  {
    suspend operator fun invoke(aiMessages: List<AiMessage>): Resource<AiMessage, DataError.Network> {
        return aiChatRepository.sendMessage(aiMessages)
    }
}
