package com.pegio.gymbro.data.remote.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiChatRequestDto(
    @SerialName("model") val model: String = "gpt-4o-mini",
    @SerialName("messages") val messages: List<MessageDto>,
    @SerialName("max_tokens") val maxTokens: Int = 300
)

@Serializable
data class MessageDto(
    @SerialName("role") val role: String = "user",
    @SerialName("content") val content: List<ContentDto>
)


@OptIn(ExperimentalSerializationApi::class)
@Serializable
@kotlinx.serialization.json.JsonClassDiscriminator("contentType")
sealed class ContentDto {

    @Serializable
    @SerialName("text")
    data class TextContentDto(
        @SerialName("type") val type: String = "text",
        @SerialName("text") val text: String
    ) : ContentDto()

    @Serializable
    @SerialName("image_url")
    data class ImageUrlContentDto(
        @SerialName("type") val type: String = "image_url",
        @SerialName("image_url") val imageUrl: ImageUrlDto
    ) : ContentDto()
}



@Serializable
data class ImageUrlDto(
    @SerialName("url") val url: String
)