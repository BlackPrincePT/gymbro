package com.pegio.gymbro.domain.usecase.ai_chat

import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.AiMessagesRepository
import javax.inject.Inject

class SaveFireStoreMessagesUseCase @Inject constructor(
    private val aiMessagesRepository: AiMessagesRepository
) {
    operator fun invoke(userId: String, aiChatMessage: AiMessage){
        aiMessagesRepository.saveMessagesInFireStore(userId = userId, aiChatMessage = aiChatMessage)
    }
}