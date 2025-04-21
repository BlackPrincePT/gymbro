package com.pegio.feed.presentation.model

import com.pegio.common.presentation.model.UiUser
import com.pegio.model.Vote

data class UiPost(
    val id: String,
    val author: UiUser,
    val content: String,
    val imageUrl: String?,
    val voteCount: String,
    val currentUserVote: Vote?,
    val commentCount: String,
    val ratingAverage: String,
    val ratingCount: String,
    val publishedDate: String
) {

    val hasImage: Boolean
        get() = imageUrl != null

    companion object {

        val DEFAULT = UiPost(
            id = "",
            author = UiUser.DEFAULT,
            content = "My name is Patrick Bateman. I'm 27 years old.",
            imageUrl = null,
            voteCount = "108",
            currentUserVote = null,
            commentCount = "13",
            ratingAverage = "3.5",
            ratingCount = "23",
            publishedDate = "01/04/25"
        )
    }
}