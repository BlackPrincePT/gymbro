package com.pegio.gymbro.domain.model

data class AiMessage(
    val id: String,
    val text: String,
    val imageUrl: String?,
    val isFromUser: Boolean,
    val timestamp: Long
)