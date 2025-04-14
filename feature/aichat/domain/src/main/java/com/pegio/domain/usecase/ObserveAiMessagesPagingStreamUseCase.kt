package com.pegio.domain.usecase

import com.pegio.domain.repository.AiMessagesRepository
import javax.inject.Inject

class ObserveAiMessagesPagingStreamUseCase @Inject constructor(private val aiMessagesRepository: AiMessagesRepository) {
    operator fun invoke(userId: String, lastMessageId: Long? = null) =
        aiMessagesRepository.observeAiMessagesPagingStream(userId, lastMessageId)
}