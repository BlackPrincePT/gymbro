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
import com.pegio.gymbro.domain.model.AiChatMessage
import com.pegio.gymbro.domain.repository.AiFireStoreMessagesRepository
import javax.inject.Inject

class AiFireStoreMessagesRepositoryImpl @Inject constructor(
    private val aiChatMessageDtoMapper: AiChatMessageDtoMapper,

    ) : AiFireStoreMessagesRepository {

    private val db = Firebase.firestore

    override fun getAiMessagesPagingSource(userId: String): PagingSource<QuerySnapshot, AiChatMessage> {
        val query = db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .orderBy(TIMESTAMP)

        return getAiMessagesPagingSource(query)
    }

    override fun saveMessagesInFireStore(userId: String, aiChatMessage: AiChatMessage) {
        db.collection(USERS)
            .document(userId)
            .collection(AI_MESSAGES)
            .add(aiChatMessageDtoMapper.mapFromDomain(aiChatMessage))
    }

    private fun getAiMessagesPagingSource(query: Query): PagingSource<QuerySnapshot, AiChatMessage> {
        return FirestorePagingSource(
            query = query,
            klass = AiChatMessageDto::class.java,
            dtoMapper = aiChatMessageDtoMapper
        )
    }
}