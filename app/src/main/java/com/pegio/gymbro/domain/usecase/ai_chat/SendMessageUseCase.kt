package com.pegio.gymbro.domain.usecase.ai_chat

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.AiChatRequest
import com.pegio.gymbro.domain.model.AiChatResponse
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
)  {

    suspend operator fun invoke(aiMessages: List<AiMessage>): Resource<AiChatResponse, DataError.Network> {
        // This needs to be discussed, where this should be and what should be written in content
        val systemMessage = AiMessage(role = "system", content = "You are a professional gym assistant in the app called GymBro.")

        val aiChatRequest = AiChatRequest(
            aiMessages = listOf(systemMessage) + aiMessages
        )

        return chatRepository.sendMessage(aiChatRequest)
    }
}
