package com.example.domain.usecase

import com.example.domain.repository.AiMessagesRepository
import com.example.model.AiMessage
import javax.inject.Inject

class SaveFireStoreMessagesUseCase @Inject constructor(
    private val aiMessagesRepository: AiMessagesRepository
) {
    operator fun invoke(userId: String, aiChatMessage: AiMessage){
        aiMessagesRepository.saveMessagesInFireStore(userId = userId, aiChatMessage = aiChatMessage)
    }
}