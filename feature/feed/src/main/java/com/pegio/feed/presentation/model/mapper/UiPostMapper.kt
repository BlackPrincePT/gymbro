package com.pegio.feed.presentation.model.mapper

import com.pegio.common.core.FromDomainMapper
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.domain.model.PostWithAuthorAndVote
import com.pegio.feed.presentation.model.UiPost
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UiPostMapper @Inject constructor(
    private val uiUserMapper: UiUserMapper
) : FromDomainMapper<UiPost, PostWithAuthorAndVote> {

    override fun mapFromDomain(data: PostWithAuthorAndVote): UiPost {
        val (post, author, currentUserVote) = data
        return UiPost(
            id = post.id,
            author = author?.let(uiUserMapper::mapFromDomain) ?: UiUser.EMPTY,
            content = post.content,
            imageUrl = post.imageUrl,
            workoutId = post.workoutId,
            voteCount = post.voteCount.toString(),
            currentUserVote = currentUserVote,
            commentCount = post.commentCount.toString(),
            ratingAverage = post.ratingAverage.toString(),
            ratingCount = post.ratingCount.toString(),
            publishedDate = convertEpochToString(post.timestamp)
        )
    }

    private fun convertEpochToString(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return localDateTime.toJavaLocalDateTime().format(formatter) // FIXME PLEASE
    }
}