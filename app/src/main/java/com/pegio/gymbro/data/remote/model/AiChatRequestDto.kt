package com.pegio.gymbro.data.remote.model

import com.pegio.gymbro.data.remote.core.OpenAiConstants.GPT_MODEL
import com.pegio.gymbro.data.remote.core.OpenAiConstants.IMAGE_URL
import com.pegio.gymbro.data.remote.core.OpenAiConstants.MAX_TOKENS
import com.pegio.gymbro.data.remote.core.OpenAiConstants.TEXT
import com.pegio.gymbro.data.remote.core.OpenAiConstants.USER
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class AiChatRequestDto(
    @SerialName("model") val model: String = GPT_MODEL,
    @SerialName("messages") val messages: List<MessageDto>,
    @SerialName("max_tokens") val maxTokens: Int = MAX_TOKENS
)

@Serializable
data class MessageDto(
    @SerialName("role") val role: String = USER,
    @SerialName("content") val content: List<ContentDto>
)

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
sealed class ContentDto {

    @Serializable
    @SerialName(TEXT)
    data class TextContentDto(
        @SerialName("text") val text: String
    ) : ContentDto()

    @Serializable
    @SerialName(IMAGE_URL)
    data class ImageUrlContentDto(
        @SerialName("image_url") val imageUrl: ImageUrlDto
    ) : ContentDto()
}

@Serializable
data class ImageUrlDto(
    @SerialName("url") val url: String
)