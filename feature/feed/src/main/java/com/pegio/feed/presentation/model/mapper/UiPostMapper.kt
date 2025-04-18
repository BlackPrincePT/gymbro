package com.pegio.feed.presentation.model.mapper

import com.pegio.common.core.FromDomainMapper
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.feed.presentation.model.UiPost
import com.pegio.model.Post
import com.pegio.model.User
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UiPostMapper @Inject constructor(
    private val uiUserMapper: UiUserMapper
) : FromDomainMapper<UiPost, Pair<Post, User?>> {

    override fun mapFromDomain(data: Pair<Post, User?>): UiPost {
        val (post, author) = data
        return UiPost(
            id = post.id,
            author = author?.let(uiUserMapper::mapFromDomain) ?: UiUser.EMPTY,
            content = post.content,
            imageUrl = post.imageUrl,
            voteCount = post.voteCount.toString(),
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