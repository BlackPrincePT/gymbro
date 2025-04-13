package com.example.data.model.mapper

import com.example.data.core.OpenAiConstants
import com.example.model.AiMessage
import com.example.network.model.AiChatRequestDto
import com.example.network.model.ContentDto
import com.example.network.model.ImageUrlDto
import com.example.network.model.MessageDto
import com.pegio.common.core.FromDomainMapper
import javax.inject.Inject

class AiChatRequestDtoMapper @Inject constructor() :
    FromDomainMapper<AiChatRequestDto, List<AiMessage>> {

    override fun mapFromDomain(data: List<AiMessage>): AiChatRequestDto {
        val messages = data.map { aiMessage ->
            val textContentDto = convertText(aiMessage.text)
            val imageUrlContentDto = convertImageUrl(aiMessage.imageUrl)

            createMessageDto(textContentDto, imageUrlContentDto)
        }

        return AiChatRequestDto(
            model = OpenAiConstants.GPT_MODEL,
            messages = messages,
            maxTokens = OpenAiConstants.MAX_TOKENS
        )
    }

    private fun convertText(text: String): ContentDto.TextContentDto {
        return ContentDto.TextContentDto(text = text)
    }

    private fun convertImageUrl(imageUrl: String?): ContentDto.ImageUrlContentDto? {
        return imageUrl?.let {
            val imageUrlDto = ImageUrlDto(url = imageUrl)
            ContentDto.ImageUrlContentDto(imageUrl = imageUrlDto)
        }
    }

    private fun createMessageDto(
        textContentDto: ContentDto.TextContentDto,
        imageUrlContentDto: ContentDto.ImageUrlContentDto?
    ): MessageDto {
        val content = listOfNotNull(textContentDto, imageUrlContentDto)
        return MessageDto(role = OpenAiConstants.USER, content = content)
    }
}