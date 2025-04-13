package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId

data class AiChatMessageDto(
    @DocumentId val id: String? = null,
    val text: String = "",
    val imageUrl: String? = null,
    val fromUser: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)