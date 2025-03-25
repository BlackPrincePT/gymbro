package com.pegio.gymbro.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiChatRequestDto(
    @SerialName("model") val model: String,
    @SerialName("messages") val messages: List<MessageDto>
)

@Serializable
data class MessageDto(
    @SerialName("role") val role: String,
    @SerialName("content") val content: String
)