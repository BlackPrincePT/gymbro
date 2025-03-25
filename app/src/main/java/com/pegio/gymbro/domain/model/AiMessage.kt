package com.pegio.gymbro.domain.model

data class AiMessage(
    val role: String = "assistant",
    val content: String
)