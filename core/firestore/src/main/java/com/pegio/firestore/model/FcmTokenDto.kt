package com.pegio.firestore.model

import com.google.firebase.firestore.DocumentId

data class FcmTokenDto(
    @DocumentId val id: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)