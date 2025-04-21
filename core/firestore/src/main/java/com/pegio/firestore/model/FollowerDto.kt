package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId

data class FollowerDto(
    @DocumentId val id: String? = null,
    val timestamp: Long = 0L
)