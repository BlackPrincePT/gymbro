package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.StoryDto
import com.pegio.model.Media
import com.pegio.model.Story
import javax.inject.Inject

class StoryDtoMapper @Inject constructor() : Mapper<StoryDto, Story> {

    override fun mapToDomain(data: StoryDto): Story = with(data) {
        return Story(
            id = id ?: throw IllegalStateException(),
            authorId = authorId,
            media = Media(url = mediaUrl, type = mediaType),
            createdAt = createdAt,
            expiresAt = expiresAt
        )
    }

    override fun mapFromDomain(data: Story): StoryDto = with(data) {
        return StoryDto(
            id = id,
            authorId = authorId,
            mediaUrl = media.url,
            mediaType = media.type,
            createdAt = createdAt,
            expiresAt = expiresAt
        )
    }
}