package com.pegio.gymbro.domain.usecase.ai_chat

import com.pegio.gymbro.domain.repository.AiMessagesRepository
import javax.inject.Inject

class ObserveAiMessagesPagingStreamUseCase @Inject constructor(private val aiMessagesRepository: AiMessagesRepository) {
    operator fun invoke(userId: String, lastMessageId: Long? = null) =
        aiMessagesRepository.getAiMessagesPagingSource(userId, lastMessageId)
}