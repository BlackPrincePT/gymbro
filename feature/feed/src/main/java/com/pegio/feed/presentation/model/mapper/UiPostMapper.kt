package com.pegio.feed.presentation.model.mapper

import com.pegio.common.core.FromDomainMapper
import com.pegio.common.presentation.model.UiUser
import com.pegio.common.presentation.model.mapper.UiUserMapper
import com.pegio.common.presentation.util.DateUtils
import com.pegio.domain.model.PostWithAuthorAndVote
import com.pegio.feed.presentation.model.UiPost
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
            publishedDate =  DateUtils.formatRelativeTime(post.timestamp)
        )
    }
}