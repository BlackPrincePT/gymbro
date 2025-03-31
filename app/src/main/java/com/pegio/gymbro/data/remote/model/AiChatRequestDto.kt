package com.pegio.gymbro.data.remote.model

import com.pegio.gymbro.data.remote.core.OpenAiConstants.GPT_MODEL
import com.pegio.gymbro.data.remote.core.OpenAiConstants.IMAGE_URL
import com.pegio.gymbro.data.remote.core.OpenAiConstants.MAX_TOKENS
import com.pegio.gymbro.data.remote.core.OpenAiConstants.TEXT
import com.pegio.gymbro.data.remote.core.OpenAiConstants.USER
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
@kotlinx.serialization.json.JsonClassDiscriminator(discriminator = "contentType")
sealed class ContentDto {

    @Serializable
    data class TextContentDto(
        @SerialName("type") val type: String = TEXT,
        @SerialName("text") val text: String
    ) : ContentDto()

    @Serializable
    data class ImageUrlContentDto(
        @SerialName("type") val type: String = IMAGE_URL,
        @SerialName("image_url") val imageUrl: ImageUrlDto
    ) : ContentDto()
}

@Serializable
data class ImageUrlDto(
    @SerialName("url") val url: String
)