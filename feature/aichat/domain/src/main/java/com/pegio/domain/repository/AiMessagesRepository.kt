package com.pegio.domain.repository

import com.pegio.model.AiMessage
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import kotlinx.coroutines.flow.Flow

interface AiMessagesRepository {
    fun observeAiMessagesPagingStream(userId: String, earliestMessageTimestamp: Long?): Flow<Resource<List<AiMessage>, DataError.Firestore>>
    fun saveMessagesInFireStore(userId: String, aiChatMessage: AiMessage)
}
