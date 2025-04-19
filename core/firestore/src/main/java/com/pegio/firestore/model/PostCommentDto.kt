package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId

data class PostCommentDto(
    @DocumentId val id: String? = null,
    val authorId: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)
