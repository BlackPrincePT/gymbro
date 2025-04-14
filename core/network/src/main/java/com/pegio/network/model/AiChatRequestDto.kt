package com.pegio.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class AiChatRequestDto(
    @SerialName("model") val model: String,
    @SerialName("messages") val messages: List<MessageDto>,
    @SerialName("max_tokens") val maxTokens: Int
)

@Serializable
data class MessageDto(
    @SerialName("role") val role: String,
    @SerialName("content") val content: List<ContentDto>
)

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
sealed class ContentDto {

    @Serializable
    @SerialName("text")
    data class TextContentDto(
        @SerialName("text") val text: String
    ) : ContentDto()

    @Serializable
    @SerialName("image_url")
    data class ImageUrlContentDto(
        @SerialName("image_url") val imageUrl: ImageUrlDto
    ) : ContentDto()
}

@Serializable
data class ImageUrlDto(
    @SerialName("url") val url: String
)