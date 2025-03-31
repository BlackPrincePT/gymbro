package com.pegio.gymbro.data.repository

import androidx.paging.PagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.pegio.gymbro.data.remote.core.FirebaseConstants.AI_MESSAGES
import com.pegio.gymbro.data.remote.core.FirebaseConstants.TIMESTAMP
import com.pegio.gymbro.data.remote.core.FirebaseConstants.USERS
import com.pegio.gymbro.data.remote.core.FirestorePagingSource
import com.pegio.gymbro.data.remote.mapper.AiChatMessageDtoMapper
import com.pegio.gymbro.data.remote.model.AiChatMessageDto
import com.pegio.gymbro.domain.model.AiMessage
import com.pegio.gymbro.domain.repository.AiMessagesRepository
import javax.inject.Inject

class AiMessagesRepositoryImpl @Inject constructor(
    private val aiChatMessageDtoMapper: AiChatMessageDtoMapper
) : AiMessagesRepository {

    private val db = Firebase.firestore

    override fun getAiMessagesPagingSource(userId: String): PagingSource<QuerySnapshot, AiMessage> {
        val query = db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .orderBy(TIMESTAMP)

        return getAiMessagePagingSource(query)
    }

    override fun saveMessagesInFireStore(userId: String, aiChatMessage: AiMessage) {
        db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .add(aiChatMessageDtoMapper.mapFromDomain(aiChatMessage))
    }

    private fun getAiMessagePagingSource(query: Query): PagingSource<QuerySnapshot, AiMessage> {
        return FirestorePagingSource(
            query = query,
            klass = AiChatMessageDto::class.java,
            mapper = aiChatMessageDtoMapper
        )
    }
}