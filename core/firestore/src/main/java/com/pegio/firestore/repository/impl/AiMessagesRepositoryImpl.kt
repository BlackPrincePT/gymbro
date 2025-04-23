package com.pegio.firestore.repository.impl

import com.pegio.firestore.model.mapper.AiChatMessageDtoMapper
import com.pegio.firestore.repository.AiMessagesRepository
import com.pegio.model.AiMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.pegio.common.core.DataError
import com.pegio.common.core.Resource
import com.pegio.common.core.convertList
import com.pegio.common.core.onSuccess
import com.pegio.firestore.core.FirestoreConstants.AI_MESSAGES
import com.pegio.firestore.core.FirestoreConstants.TIMESTAMP
import com.pegio.firestore.core.FirestoreConstants.USERS
import com.pegio.firestore.model.AiChatMessageDto
import com.pegio.firestore.util.FirestoreUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AiMessagesRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firestoreUtils: FirestoreUtils,
    private val aiChatMessageDtoMapper: AiChatMessageDtoMapper
) : AiMessagesRepository {

    private var lastVisibleTimestamp: Long? = Long.MAX_VALUE

    companion object {
        private const val MESSAGES_PAGE_SIZE: Long = 30L
    }

    override fun observeAiMessagesPagingStream(userId: String, earliestMessageTimestamp: Long?): Flow<Resource<List<AiMessage>, DataError.Firestore>> {
        val query = db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .startAfter(earliestMessageTimestamp)
            .limit(MESSAGES_PAGE_SIZE)

        return firestoreUtils.observeDocuments(query, AiChatMessageDto::class.java)
            .onSuccess { lastVisibleTimestamp = it.lastOrNull()?.timestamp }
            .convertList(aiChatMessageDtoMapper::mapToDomain)
    }

    override fun saveMessagesInFireStore(userId: String, aiChatMessage: AiMessage) {
        db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .add(aiChatMessageDtoMapper.mapFromDomain(aiChatMessage))
    }
}