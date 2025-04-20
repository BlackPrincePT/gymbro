package com.pegio.firestore.model.mapper

import com.pegio.common.core.Mapper
import com.pegio.firestore.model.PostCommentDto
import com.pegio.model.PostComment
import javax.inject.Inject

internal class PostCommentDtoMapper @Inject constructor() : Mapper<PostCommentDto, PostComment> {

    override fun mapToDomain(data: PostCommentDto): PostComment {
        return PostComment(
            id = data.id ?: throw IllegalStateException(),
            authorId = data.authorId,
            content = data.content,
            timestamp = data.timestamp
        )
    }

    override fun mapFromDomain(data: PostComment): PostCommentDto {
        return PostCommentDto(
            id = data.id,
            authorId = data.authorId,
            content = data.content,
            timestamp = data.timestamp
        )
    }
}