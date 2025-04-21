package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId
import com.pegio.model.Media

data class StoryDto(
    @DocumentId val id: String? = null,
    val authorId: String = "",
    val mediaUrl: String = "",
    val mediaType: Media.Type = Media.Type.IMAGE,
    val createdAt: Long = 0L,
    val expiresAt: Long = 0L
)