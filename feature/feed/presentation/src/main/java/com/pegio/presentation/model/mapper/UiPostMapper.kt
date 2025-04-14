package com.pegio.presentation.model.mapper

import com.example.model.Post
import com.pegio.common.core.FromDomainMapper
import com.pegio.presentation.model.UiPost
import com.pegio.presentation.model.UiUser
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UiPostMapper @Inject constructor() : FromDomainMapper<UiPost, Post> {

    override fun mapFromDomain(data: Post): UiPost {
        return UiPost(
            id = data.id,
            author = UiUser.DEFAULT,
            content = data.content,
            imageUrl = data.imageUrl,
            voteCount = data.voteCount.toString(),
            commentCount = data.commentCount.toString(),
            ratingAverage = data.ratingAverage.toString(),
            ratingCount = data.ratingCount.toString(),
            publishedDate = convertEpochToString(data.timestamp)
        )
    }

    private fun convertEpochToString(epochMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return localDateTime.toJavaLocalDateTime().format(formatter) // FIXME PLEASE
    }
}