package com.pegio.gymbro.data.remote.model.mapper

import com.pegio.gymbro.data.remote.model.PostDto
import com.pegio.gymbro.data.remote.model.util.InvalidDocumentIdException
import com.pegio.gymbro.domain.core.Mapper
import com.pegio.gymbro.domain.model.Post
import javax.inject.Inject

class PostDtoMapper @Inject constructor() : Mapper<PostDto, Post> {

    override fun mapToDomain(data: PostDto): Post {
        return Post(
            id = data.id ?: throw InvalidDocumentIdException(),
            authorId = data.authorId,
            content = data.content,
            imageUrl = data.imageUrl,
            upVotesInLast24Hours = data.upVotesInLast24Hours,
            voteCount = data.voteCount,
            commentCount = data.commentCount,
            timestamp = data.timestamp
        )
    }

    override fun mapFromDomain(data: Post): PostDto {
        return PostDto(
            id = data.id,
            authorId = data.authorId,
            content = data.content,
            imageUrl = data.imageUrl,
            upVotesInLast24Hours = data.upVotesInLast24Hours,
            voteCount = data.voteCount,
            commentCount = data.commentCount,
            timestamp = data.timestamp
        )
    }
}