package com.pegio.gymbro.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiChatResponseDto(
    @SerialName("choices") val choices: List<ChoiceDto>
)

@Serializable
data class ChoiceDto(
    @SerialName("message") val message: ResponseMessageDto
)

@Serializable
data class ResponseMessageDto(
    @SerialName("role") val role: String = "assistant",
    @SerialName("content") val content: String
)
