package com.pegio.gymbro.domain.repository

import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.model.AiMessage
import kotlinx.coroutines.flow.Flow

interface AiMessagesRepository {
    fun getAiMessagesPagingSource(userId: String, earliestMessageTimestamp: Long?): Flow<Resource<List<AiMessage>, DataError.Firestore>>
    fun saveMessagesInFireStore(userId: String, aiChatMessage: AiMessage)
}
