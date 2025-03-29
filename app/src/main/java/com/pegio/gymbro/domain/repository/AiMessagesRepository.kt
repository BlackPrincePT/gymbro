package com.pegio.gymbro.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import com.pegio.gymbro.domain.model.AiChatMessage

interface AiFireStoreMessagesRepository {
    fun getAiMessagesPagingSource(userId: String) : PagingSource<QuerySnapshot, AiChatMessage>
    fun saveMessagesInFireStore(userId: String,aiChatMessage:AiChatMessage)
}
