package com.example.domain.usecase

import com.example.domain.repository.AiMessagesRepository
import javax.inject.Inject

class ObserveAiMessagesPagingStreamUseCase @Inject constructor(private val aiMessagesRepository: AiMessagesRepository) {
    operator fun invoke(userId: String, lastMessageId: Long? = null) =
        aiMessagesRepository.observeAiMessagesPagingStream(userId, lastMessageId)
}