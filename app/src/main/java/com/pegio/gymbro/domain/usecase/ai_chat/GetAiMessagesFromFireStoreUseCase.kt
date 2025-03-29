package com.pegio.gymbro.domain.usecase.ai_chat

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import com.pegio.gymbro.domain.model.AiChatMessage
import com.pegio.gymbro.domain.repository.AiFireStoreMessagesRepository
import javax.inject.Inject

class GetAiMessagesUseCase @Inject constructor(
    private val aiFireStoreMessagesRepository: AiFireStoreMessagesRepository
) {

    operator fun invoke(userId: String): PagingSource<QuerySnapshot, AiChatMessage> {
        return aiFireStoreMessagesRepository.getAiMessagesPagingSource(userId)
    }
}