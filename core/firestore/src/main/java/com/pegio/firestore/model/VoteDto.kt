package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId

data class VoteDto(
    @DocumentId val id: String? = null,
    val vote: Int = 0,
    val timestamp: Long = 0L
)