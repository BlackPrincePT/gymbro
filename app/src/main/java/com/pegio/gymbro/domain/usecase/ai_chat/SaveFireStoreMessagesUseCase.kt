package com.pegio.gymbro.domain.usecase.ai_chat

import com.pegio.gymbro.domain.model.AiChatMessage
import com.pegio.gymbro.domain.repository.AiFireStoreMessagesRepository
import javax.inject.Inject

class SaveFireStoreMessagesUseCase @Inject constructor(
    private val aiFireStoreMessagesRepository: AiFireStoreMessagesRepository
) {
    operator fun invoke(userId: String, aiChatMessage: AiChatMessage){
        aiFireStoreMessagesRepository.saveMessagesInFireStore(userId = userId, aiChatMessage = aiChatMessage)
    }
}