package com.pegio.firestore.model.mapper

import com.pegio.model.Post
import com.pegio.common.core.Mapper
import com.pegio.firestore.model.PostDto
import javax.inject.Inject

class PostDtoMapper @Inject constructor() : Mapper<PostDto, Post> {

    override fun mapToDomain(data: PostDto): Post {
        return Post(
            id = data.id ?: throw Exception(),
            authorId = data.authorId,
            content = data.content,
            imageUrl = data.imageUrl,
            upVotesInLast24Hours = data.upVotesInLast24Hours,
            voteCount = data.voteCount,
            commentCount = data.commentCount,
            ratingAverage = data.ratingAverage,
            ratingCount = data.ratingCount,
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
            ratingAverage = data.ratingAverage,
            ratingCount = data.ratingCount,
            timestamp = data.timestamp
        )
    }
}