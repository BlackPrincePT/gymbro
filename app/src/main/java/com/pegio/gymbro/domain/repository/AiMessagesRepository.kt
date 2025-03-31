package com.pegio.gymbro.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import com.pegio.gymbro.domain.model.AiMessage

interface AiMessagesRepository {
    fun getAiMessagesPagingSource(userId: String) : PagingSource<QuerySnapshot, AiMessage>
    fun saveMessagesInFireStore(userId: String,aiChatMessage:AiMessage)
}
