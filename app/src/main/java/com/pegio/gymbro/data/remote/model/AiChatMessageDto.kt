package com.pegio.gymbro.data.remote.model

import com.google.firebase.firestore.DocumentId
import com.pegio.gymbro.presentation.core.DateUtil

data class AiChatMessageDto(
    @DocumentId val id: String? = null,
    val text: String,
    val date: String = DateUtil.todayDate(),
    val imageUri : String? = null,
    val isFromUser: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)
