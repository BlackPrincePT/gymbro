package com.pegio.domain.usecase.aichat

import com.pegio.domain.repository.AiMessagesRepository
import com.pegio.model.AiMessage
import javax.inject.Inject

class SaveFireStoreMessagesUseCase @Inject constructor(
    private val aiMessagesRepository: AiMessagesRepository
) {
    operator fun invoke(userId: String, aiChatMessage: AiMessage){
        aiMessagesRepository.saveMessagesInFireStore(userId = userId, aiChatMessage = aiChatMessage)
    }
}