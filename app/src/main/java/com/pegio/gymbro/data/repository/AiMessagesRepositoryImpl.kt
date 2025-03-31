package com.pegio.gymbro.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.pegio.gymbro.data.remote.core.FirebaseConstants.AI_MESSAGES
import com.pegio.gymbro.data.remote.core.FirebaseConstants.TIMESTAMP
import com.pegio.gymbro.data.remote.core.FirebaseConstants.USERS
import com.pegio.gymbro.data.remote.core.FirestoreUtils
import com.pegio.gymbro.data.remote.mapper.AiChatMessageDtoMapper
import com.pegio.gymbro.data.remote.model.AiChatMessageDto
import com.pegio.gymbro.domain.core.DataError
import com.pegio.gymbro.domain.core.Resource
import com.pegio.gymbro.domain.core.convertList
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.AiMessagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AiMessagesRepositoryImpl @Inject constructor(
    private val firestoreUtils: FirestoreUtils,
    private val aiChatMessageDtoMapper: AiChatMessageDtoMapper
) : AiMessagesRepository {

    private val db = Firebase.firestore

    companion object {
        private const val MESSAGES_PAGE_SIZE: Long = 30L
    }

    override fun getAiMessagesPagingSource(userId: String, earliestMessageTimestamp: Long?): Flow<Resource<List<AiMessage>, DataError.Firestore>> {
        val query = db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .startAfter(earliestMessageTimestamp)
            .limit(MESSAGES_PAGE_SIZE)

        return firestoreUtils.observeDocuments(query, AiChatMessageDto::class.java)
            .convertList(aiChatMessageDtoMapper::mapToDomain)
    }

    override fun saveMessagesInFireStore(userId: String, aiChatMessage: AiMessage) {
        db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .add(aiChatMessageDtoMapper.mapFromDomain(aiChatMessage))
    }
}