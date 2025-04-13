package com.example.domain.repository

import com.example.model.AiMessage
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface AiMessagesRepository {
    fun observeAiMessagesPagingStream(userId: String, earliestMessageTimestamp: Long?): Flow<Resource<List<AiMessage>, DataError.Firestore>>
    fun saveMessagesInFireStore(userId: String, aiChatMessage: AiMessage)
}
