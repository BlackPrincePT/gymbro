package com.pegio.domain.usecase

import com.pegio.domain.repository.AiChatRepository
import com.pegio.model.AiMessage
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
