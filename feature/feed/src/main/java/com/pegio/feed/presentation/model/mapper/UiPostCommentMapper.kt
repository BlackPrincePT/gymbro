package com.pegio.feed.presentation.model.mapper

import com.pegio.common.core.FromDomainMapper
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.feed.presentation.model.UiPostComment
import com.pegio.model.PostComment
import com.pegio.model.User
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UiPostCommentMapper @Inject constructor(
    private val uiUserMapper: UiUserMapper
): FromDomainMapper<UiPostComment, Pair<PostComment, User?>> {

    override fun mapFromDomain(data: Pair<PostComment, User?>): UiPostComment {
        val (comment, author) = data
        return UiPostComment(
            id = comment.id,
            author = author?.let(uiUserMapper::mapFromDomain) ?: UiUser.EMPTY,
            content = comment.content,
            date = convertEpochToString(comment.timestamp)
        )
    }

    private fun convertEpochToString(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return localDateTime.toJavaLocalDateTime().format(formatter) // FIXME PLEASE
    }
}