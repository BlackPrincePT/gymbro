package com.pegio.gymbro.domain.usecase.ai_chat

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.AiMessagesRepository
import javax.inject.Inject

class GetAiMessagesUseCase @Inject constructor(
    private val aiMessagesRepository: AiMessagesRepository
) {

    operator fun invoke(userId: String): PagingSource<QuerySnapshot, AiMessage> {
        return aiMessagesRepository.getAiMessagesPagingSource(userId)
    }
}