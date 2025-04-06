package com.pegio.gymbro.data.remote.model.mapper

import com.pegio.gymbro.data.remote.model.AiChatRequestDto
import com.pegio.gymbro.data.remote.model.ContentDto
import com.pegio.gymbro.data.remote.model.ImageUrlDto
import com.pegio.gymbro.data.remote.model.MessageDto
import com.pegio.gymbro.domain.core.FromDomainMapper
import com.pegio.gymbro.domain.model.AiMessage
import javax.inject.Inject

class AiChatRequestDtoMapper @Inject constructor() : FromDomainMapper<AiChatRequestDto, List<AiMessage>> {

    override fun mapFromDomain(data: List<AiMessage>): AiChatRequestDto {
        val messages = data.map { aiMessage ->
            val textContentDto = convertText(aiMessage.text)
            val imageUrlContentDto = convertImageUrl(aiMessage.imageUrl)

            createMessageDto(textContentDto, imageUrlContentDto)
        }

        return AiChatRequestDto(messages = messages)
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
        return MessageDto(content = content)
    }
}