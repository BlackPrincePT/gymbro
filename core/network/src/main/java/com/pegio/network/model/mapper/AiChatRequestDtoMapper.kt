package com.pegio.network.model.mapper

import com.pegio.model.AiMessage
import com.pegio.network.model.AiChatRequestDto
import com.pegio.network.model.ContentDto
import com.pegio.network.model.ImageUrlDto
import com.pegio.network.model.MessageDto
import com.pegio.common.core.FromDomainMapper
import com.pegio.network.core.OpenAiConstants
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